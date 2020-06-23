package com.commodorethrawn.strawgolem.entity.capability.memory;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Memory implements IMemory {

    private final List<BlockPos> posList;
    private BlockPos priority;

    public Memory() {
        posList = new ArrayList<>();
        priority = BlockPos.ZERO;
    }

    @Override
    public List<BlockPos> getPositionList() {
        return posList;
    }

    @Override
    public BlockPos getClosestPosition(BlockPos pos) {
        if (posList.size() == 0) return BlockPos.ZERO;
        BlockPos closest = posList.get(0);
        for (int i = 1; i < posList.size(); ++i) {
            if (pos.distanceSq(closest) > pos.distanceSq(posList.get(i))) {
                closest = posList.get(i);
            }
        }
        return closest;
    }

    @Override
    public boolean containsPosition(BlockPos pos) {
        return posList.contains(pos);
    }

    @Override
    public void addPosition(BlockPos pos) {
        posList.add(pos);
    }

    @Override
    public void removePosition(BlockPos pos) {
        posList.remove(pos);
    }

    @Override
    public boolean hasPriorityChest() {
        return priority != BlockPos.ZERO;
    }

    @Override
    public BlockPos getPriorityChest() {
        return priority;
    }

    @Override
    public void setPriorityChest(BlockPos pos) {
        priority = pos;
    }

}