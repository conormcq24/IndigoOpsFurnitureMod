package indigoops.indigooperationsfurnituremod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ChairEntity extends Entity {

    public ChairEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Vec3d getPassengerRidingPos(Entity passenger) {
        return new Vec3d(this.getX(), this.getY() + 0.6, this.getZ());
    }

    @Override
    protected void removePassenger(Entity passenger){
        super.removePassenger(passenger);
        this.kill();
    }
}
