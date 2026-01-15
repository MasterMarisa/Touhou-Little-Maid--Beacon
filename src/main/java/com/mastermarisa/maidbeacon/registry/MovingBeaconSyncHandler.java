package com.mastermarisa.maidbeacon.registry;

import com.mastermarisa.maidbeacon.logic.MovingBeacon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

public class MovingBeaconSyncHandler implements AttachmentSyncHandler<MovingBeacon> {

    @Override
    public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf, MovingBeacon movingBeacon, boolean b) {
        registryFriendlyByteBuf.writeInt(movingBeacon.beaconLevel);
        registryFriendlyByteBuf.writeCollection(movingBeacon.getActivatedEffectIDs(), FriendlyByteBuf::writeUtf);
    }

    @Override
    public @Nullable MovingBeacon read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable MovingBeacon movingBeacon) {
        if(movingBeacon == null){
            movingBeacon = new MovingBeacon();
        }
        movingBeacon.beaconLevel = registryFriendlyByteBuf.readInt();
        movingBeacon.setActivatedEffectIDs(registryFriendlyByteBuf.readCollection(HashSet::new,FriendlyByteBuf::readUtf));
        return movingBeacon;
    }
}
