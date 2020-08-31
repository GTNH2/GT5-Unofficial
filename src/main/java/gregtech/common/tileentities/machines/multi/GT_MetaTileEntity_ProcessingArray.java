package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ProcessingArray_Manager;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine.isValidForLowGravity;

public class GT_MetaTileEntity_ProcessingArray extends GT_MetaTileEntity_MultiBlockBase {

    private GT_Recipe mLastRecipe;
    private int tTier = 0;
    private int mMult = 0;
    private boolean mSeparate = false;

    public GT_MetaTileEntity_ProcessingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_ProcessingArray(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ProcessingArray(this.mName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Processing Array",
                "Runs supplied machines as if placed in the world",
                "Size(WxHxD): 3x3x3 (Hollow), Controller (Front centered)",
                "1x Input Hatch/Bus (Any casing)",
                "1x Output Hatch/Bus (Any casing)",
                "1x Maintenance Hatch (Any casing)",
                "1x Energy Hatch (Any casing)",
                "Robust Tungstensteel Machine Casings for the rest (14 at least!)",
                "Place up to 64 Single Block GT Machines into the Controller Inventory",
                "Use screwdriver to enable separate input busses",
                "Maximal overclockedness of machines inside: Tier 9"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][48], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_PROCESSING_ARRAY)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][48]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ProcessingArray.png");
    }

    //TODO: Expand so it also does the non recipe map recipes
    /*
    public void remoteRecipeCheck() {
        if (mInventory[1] == null) return;
        String tmp = mInventory[1].getUnlocalizedName().replaceAll("gt.blockmachines.basicmachine.", "");
        if (tmp.startsWith("replicator")) {

        } else if (tmp.startsWith("brewery")) {

        } else if (tmp.startsWith("packer")) {

        } else if (tmp.startsWith("printer")) {

        } else if (tmp.startsWith("disassembler")) {

        } else if (tmp.startsWith("massfab")) {

        } else if (tmp.startsWith("scanner")) {

        }
    }
    */

    public GT_Recipe_Map getRecipeMap() {
        if (isCorrectMachinePart(mInventory[1])) {
            return GT_ProcessingArray_Manager.getRecipeMapForMeta(mInventory[1].getItemDamage());
        }
        return null;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return aStack != null && aStack.getUnlocalizedName().startsWith("gt.blockmachines.basicmachine.");
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    private String mMachine = "";
    public boolean checkRecipe(ItemStack aStack) {
        if (!isCorrectMachinePart(mInventory[1])) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map map = getRecipeMap();
        if (map == null) return false;

        if (mInventory[1].getUnlocalizedName().endsWith("10")) {
            tTier = 9;
            mMult = 2;//u need 4x less machines and they will use 4x less power
        } else if (mInventory[1].getUnlocalizedName().endsWith("11")) {
            tTier = 9;
            mMult = 4;//u need 16x less machines and they will use 16x less power
        } else if (mInventory[1].getUnlocalizedName().endsWith("12") ||
                mInventory[1].getUnlocalizedName().endsWith("13") ||
                mInventory[1].getUnlocalizedName().endsWith("14") ||
                mInventory[1].getUnlocalizedName().endsWith("15")) {
            tTier = 9;
            mMult = 6;//u need 64x less machines and they will use 64x less power
        } else if (mInventory[1].getUnlocalizedName().endsWith("1")) {
            tTier = 1;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("2")) {
            tTier = 2;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("3")) {
            tTier = 3;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("4")) {
            tTier = 4;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("5")) {
            tTier = 5;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("6")) {
            tTier = 6;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("7")) {
            tTier = 7;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("8")) {
            tTier = 8;
            mMult = 0;//*1
        } else if (mInventory[1].getUnlocalizedName().endsWith("9")) {
            tTier = 9;
            mMult = 0;//*1
        } else {
            tTier = 0;
            mMult = 0;//*1
        }

        if (!mMachine.equals(mInventory[1].getUnlocalizedName())) mLastRecipe = null;
        mMachine = mInventory[1].getUnlocalizedName();

        ArrayList<FluidStack> tFluidList = getStoredFluids();
        FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
        if (mSeparate) {
        	ArrayList<ItemStack> tInputList = new ArrayList<>();
        	for (GT_MetaTileEntity_Hatch_InputBus tHatch : mInputBusses) {
        		IGregTechTileEntity tInpuBus = tHatch.getBaseMetaTileEntity();
        		for (int i = tInpuBus.getSizeInventory() - 1; i >= 0; i--) {
					if (tInpuBus.getStackInSlot(i) != null)
						tInputList.add(tInpuBus.getStackInSlot(i));
				}
        		ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
        		if (processRecipe(tInputs, tFluids, map))
        			return true;
        		else
        			tInputList.clear();
        	}
        } else {
        	ArrayList<ItemStack> tInputList = getStoredInputs();
        	ItemStack[] tInputs = tInputList.toArray(new ItemStack[0]);
            return processRecipe(tInputs, tFluids, map);
        }
        return false;
    }

    public boolean processRecipe(ItemStack[] tInputs, FluidStack[] tFluids, GT_Recipe.GT_Recipe_Map map) {
    	if (tInputs.length > 0 || tFluids.length > 0) {
            GT_Recipe tRecipe = map.findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);
            if (tRecipe != null) {
                if (GT_Mod.gregtechproxy.mLowGravProcessing && tRecipe.mSpecialValue == -100 &&
                        !isValidForLowGravity(tRecipe, getBaseMetaTileEntity().getWorld().provider.dimensionId))
                    return false;

                mLastRecipe = tRecipe;
                this.mEUt = 0;
                this.mOutputItems = null;
                this.mOutputFluids = null;
                int machines = Math.min(64, mInventory[1].stackSize << mMult); //Upped max Cap to 64
                int i = 0;
                for (; i < machines; i++) {
                    if (!tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                        if (i == 0) {
                            return false;
                        }
                        break;
                    }
                }
                this.mMaxProgresstime = tRecipe.mDuration;
                this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                this.mEfficiencyIncrease = 10000;
                calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, map.mAmperage, GT_Values.V[tTier]);
                //In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return false;
                this.mEUt = GT_Utility.safeInt(((long) this.mEUt * i) >> mMult, 1);
                if (mEUt == Integer.MAX_VALUE - 1)
                    return false;

                if (this.mEUt > 0) {
                    this.mEUt = (-this.mEUt);
                }
                ItemStack[] tOut = new ItemStack[tRecipe.mOutputs.length];
                for (int h = 0; h < tRecipe.mOutputs.length; h++) {
                    if (tRecipe.getOutput(h) != null) {
                        tOut[h] = tRecipe.getOutput(h).copy();
                        tOut[h].stackSize = 0;
                    }
                }
                FluidStack tFOut = null;
                if (tRecipe.getFluidOutput(0) != null) tFOut = tRecipe.getFluidOutput(0).copy();
                for (int f = 0; f < tOut.length; f++) {
                    if (tRecipe.mOutputs[f] != null && tOut[f] != null) {
                        for (int g = 0; g < i; g++) {
                            if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f))
                                tOut[f].stackSize += tRecipe.mOutputs[f].stackSize;
                        }
                    }
                }
                if (tFOut != null) {
                    int tSize = tFOut.amount;
                    tFOut.amount = tSize * i;
                }
                tOut = clean(tOut);
                this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                List<ItemStack> overStacks = new ArrayList<>();
                for (ItemStack itemStack : tOut) {
                    while (itemStack.getMaxStackSize() < itemStack.stackSize) {
                        ItemStack tmp = itemStack.copy();
                        tmp.stackSize = tmp.getMaxStackSize();
                        itemStack.stackSize = itemStack.stackSize - itemStack.getMaxStackSize();
                        overStacks.add(tmp);
                    }
                }
                if (overStacks.size() > 0) {
                    ItemStack[] tmp = new ItemStack[overStacks.size()];
                    tmp = overStacks.toArray(tmp);
                    tOut = ArrayUtils.addAll(tOut, tmp);
                }
                List<ItemStack> tSList = new ArrayList<>();
                for (ItemStack tS : tOut) {
                    if (tS.stackSize > 0) tSList.add(tS);
                }
                tOut = tSList.toArray(new ItemStack[0]);
                this.mOutputItems = tOut;
                this.mOutputFluids = new FluidStack[]{tFOut};
                updateSlots();
                return true;
            }/* else{
                ...remoteRecipeCheck()
            }*/
        }
    	return false;
    }

    public static ItemStack[] clean(final ItemStack[] v) {
        List<ItemStack> list = new ArrayList<>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new ItemStack[0]);
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
            return false;
        }
        int tAmount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if ((!addMaintenanceToMachineList(tTileEntity, 48)) && (!addInputToMachineList(tTileEntity, 48)) && (!addOutputToMachineList(tTileEntity, 48)) && (!addEnergyInputToMachineList(tTileEntity, 48))) {
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings4) {
                                return false;
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
                                return false;
                            }
                            tAmount++;
                        }
                    }
                }
            }
        }
        return tAmount >= 14;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
    	super.saveNBTData(aNBT);
    	aNBT.setBoolean("mSeparate", mSeparate);
    }

    @Override
	public void loadNBTData(final NBTTagCompound aNBT) {
    	super.loadNBTData(aNBT);
    	mSeparate = aNBT.getBoolean("mSeparate");
    }

    @Override
	public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    	mSeparate = !mSeparate;
    	GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.machines.separatebus") +" "+mSeparate);
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }


    @Override
    public String[] getInfoData() {
        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
        		StatCollector.translateToLocal("GT5U.multiblock.Progress")+": "+
        				EnumChatFormatting.GREEN + mProgresstime / 20 + EnumChatFormatting.RESET +" s / "+
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET +" s",
                StatCollector.translateToLocal("GT5U.multiblock.energy")+": "+
                		EnumChatFormatting.GREEN + storedEnergy + EnumChatFormatting.RESET +" EU / "+
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET +" EU",
                StatCollector.translateToLocal("GT5U.multiblock.usage")+": "+
                        EnumChatFormatting.RED + -mEUt + EnumChatFormatting.RESET + " EU/t",
                StatCollector.translateToLocal("GT5U.multiblock.mei")+": "+
                        EnumChatFormatting.YELLOW+ getMaxInputVoltage() +EnumChatFormatting.RESET+ " EU/t(*2A) "+StatCollector.translateToLocal("GT5U.machines.tier")+": "+
                        EnumChatFormatting.YELLOW+VN[GT_Utility.getTier(getMaxInputVoltage())]+ EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.multiblock.problems")+": "+
                        EnumChatFormatting.RED+ (getIdealStatus() - getRepairStatus())+EnumChatFormatting.RESET+
                        " "+StatCollector.translateToLocal("GT5U.multiblock.efficiency")+": "+
                        EnumChatFormatting.YELLOW+ mEfficiency / 100.0F +EnumChatFormatting.RESET + " %",
                StatCollector.translateToLocal("GT5U.PA.machinetier")+": "+
                        EnumChatFormatting.GREEN+tTier+EnumChatFormatting.RESET+
                        " "+StatCollector.translateToLocal("GT5U.PA.discount")+": "+
                        EnumChatFormatting.GREEN+(1<<mMult)+EnumChatFormatting.RESET + " x",
                StatCollector.translateToLocal("GT5U.PA.parallel")+": "+EnumChatFormatting.GREEN+((mInventory[1] != null) ? (mInventory[1].stackSize<<mMult) : 0)+EnumChatFormatting.RESET
        };
    }

}
