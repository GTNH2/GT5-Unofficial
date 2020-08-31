package gregtech.common.tileentities.machines.multi;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.ArrayList;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_LargeTurbine_HPSteam extends GT_MetaTileEntity_LargeTurbine {

    public boolean achievement = false;
    private boolean looseFit=false;

    public GT_MetaTileEntity_LargeTurbine_HPSteam(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeTurbine_HPSteam(String aName) {
        super(aName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1], aFacing == aSide ? aActive ? new GT_RenderedTexture(Textures.BlockIcons.LARGETURBINE_TI_ACTIVE5) : new GT_RenderedTexture(Textures.BlockIcons.LARGETURBINE_TI5) : Textures.BlockIcons.casingTexturePages[0][59]};
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Large High Pressure Steam Turbine",
                "Size(WxHxD): 3x3x4 (Hollow), Controller (Front centered)",
                "1x Superheated Steam Input Hatch (Side centered)",
                "1x Maintenance Hatch (Side centered)",
                "1x Dynamo Hatch (Back centered)",
                "1x Output Hatch for Steam (Side centered)",
                "Titanium Turbine Casings for the rest (24 at least!)",
                "Needs a Turbine Item (Inside controller GUI)",
                "Output depending on Rotor and fitting",
                "Use screwdriver to adjust fitting of turbine"};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeTurbine_HPSteam(mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 11;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 59;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff) {
        if(looseFit) {
            aOptFlow*=4;
            double pow = Math.pow(1.1f, ((aBaseEff - 7500) / 10000F) * 20f);
            if(aBaseEff>10000){
                aOptFlow*= pow;
                aBaseEff=7500;
            }else if(aBaseEff>7500){
                aOptFlow*= pow;
                aBaseEff*=0.75f;
            }else{
                aBaseEff*=0.75f;
            }
        }
        int tEU;
        int totalFlow = 0; // Byproducts are based on actual flow
        int flow;
        int remainingFlow = GT_Utility.safeInt((long)(aOptFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for multi-hatch scenarios.
        this.realOptFlow = aOptFlow;

        storedFluid=0;
        for (int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
            String fluidName = aFluids.get(i).getFluid().getUnlocalizedName(aFluids.get(i));
            if (fluidName.equals("ic2.fluidSuperheatedSteam")) {
                flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
                this.storedFluid += aFluids.get(i).amount;
                remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                totalFlow += flow; // track total input used
                if (!achievement) {
                    try {
                        GT_Mod.achievements.issueAchievement(this.getBaseMetaTileEntity().getWorld().getPlayerEntityByName(this.getBaseMetaTileEntity().getOwnerName()), "efficientsteam");
                    } catch (Exception ignored) {
                    }
                    achievement = true;
                }
            }else if(fluidName.equals("fluid.steam") || fluidName.equals("ic2.fluidSteam") || fluidName.equals("fluid.mfr.steam.still.name")){
                depleteInput(new FluidStack(aFluids.get(i), aFluids.get(i).amount));
            }
        }
        if(totalFlow<=0)return 0;
        tEU = totalFlow;
        addOutput(GT_ModHandler.getSteam(totalFlow));
        if (totalFlow != aOptFlow) {
            float efficiency = 1.0f - Math.abs((totalFlow - aOptFlow) / (float)aOptFlow);
            //if(totalFlow>aOptFlow){efficiency = 1.0f;}
            tEU *= efficiency;
            tEU = Math.max(1, GT_Utility.safeInt((long)tEU * (long)aBaseEff / 10000L));
        } else {
            tEU = GT_Utility.safeInt((long)tEU * (long)aBaseEff / 10000L);
        }

        return tEU;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aSide == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit^=true;
            GT_Utility.sendChatToPlayer(aPlayer, looseFit ? trans("500", "Fitting: Loose - More Flow") : trans("501", "Fitting: Tight - More Efficiency"));
        }
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (looseFit && XSTR_INSTANCE.nextInt(4)==0)?0:1;
    }

    
    
    @Override
    public String[] getInfoData() {
    	super.looseFit = looseFit;
    	return super.getInfoData();
    }
    /* 
     * moved to super
     * 
        int mPollutionReduction=0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction=Math.max(tHatch.calculatePollutionReduction(100),mPollutionReduction);
            }
        }

        String tRunning = mMaxProgresstime>0 ?
                EnumChatFormatting.GREEN+"Turbine running"+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+"Turbine stopped"+EnumChatFormatting.RESET;
        String tMaintainance = getIdealStatus() == getRepairStatus() ?
                EnumChatFormatting.GREEN+"No Maintainance issues"+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+"Needs Maintainance"+EnumChatFormatting.RESET ;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
            tDura = GT_Utility.safeInt((long)(100.0f / GT_MetaGenerated_Tool.getToolMaxDamage(mInventory[1]) * (GT_MetaGenerated_Tool.getToolDamage(mInventory[1]))+1));
        }

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                // 8 Lines available for information panels
                tRunning + ": " + EnumChatFormatting.RED+mEUt+EnumChatFormatting.RESET+" EU/t", // 1 
                tMaintainance, // 2
                "Current Speed: "+EnumChatFormatting.YELLOW+(mEfficiency/100F)+EnumChatFormatting.RESET+"%", // 2 
                "Stored Energy:" + EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+ // 3 
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU", 
                "Optimal Flow: "+EnumChatFormatting.YELLOW+GT_Utility.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/t" + // 4 
                        EnumChatFormatting.YELLOW+"("+(looseFit?"Loose":"Tight")+")", // 5 
                "Fuel Remaining: "+EnumChatFormatting.GOLD+storedFluid+EnumChatFormatting.RESET+"L", // 6 
                "Turbine Damage: "+EnumChatFormatting.RED+Integer.toString(tDura)+EnumChatFormatting.RESET+"%", // 7 
                "Pollution reduced to: "+ EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %" // 8 
        };
    }
    */
    
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting",looseFit);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit=aNBT.getBoolean("turbineFitting");
    }
}
