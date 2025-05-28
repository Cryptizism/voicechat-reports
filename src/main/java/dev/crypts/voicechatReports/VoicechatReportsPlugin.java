package dev.crypts.voicechatReports;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import dev.crypts.voicechatReports.data.CircularPCMBuffer;
import dev.crypts.voicechatReports.data.PCMBufferStore;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VoicechatReportsPlugin implements VoicechatPlugin {

    OpusDecoder decoder;

    @Override
    public String getPluginId() {
        return VoicechatReports.PLUGIN_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
        this.decoder = api.createDecoder();
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
    }

    private void onMicrophone(MicrophonePacketEvent event) {
        if (event.getSenderConnection() == null) {
            return;
        }

        if (!(event.getSenderConnection().getPlayer().getPlayer() instanceof Player player)) {
            return;
        }


        UUID uuid = player.getUniqueId();

        CircularPCMBuffer opusPlayerBuffer = PCMBufferStore.getInstance().getOrCreateBuffer(uuid);

        opusPlayerBuffer.addBytes(opusRawToPCM(event.getPacket().getOpusEncodedData()));
    }

    private byte[] opusRawToPCM(byte[] opusPacket) {
        short[] pcmShorts = decoder.decode(opusPacket);

        return shortsToBytes(pcmShorts, pcmShorts.length);
    }

    private byte[] shortsToBytes(short[] shorts, int length) {
        byte[] bytes = new byte[length * 2];
        for (int i = 0; i < length; i++) {
            bytes[2 * i] = (byte) (shorts[i] & 0xff);
            bytes[2 * i + 1] = (byte) ((shorts[i] >> 8) & 0xff);
        }
        return bytes;
    }

}
