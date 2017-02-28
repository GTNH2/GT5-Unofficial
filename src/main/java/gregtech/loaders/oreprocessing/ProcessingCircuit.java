package gregtech.loaders.oreprocessing;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

public class ProcessingCircuit implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingCircuit() {
        OrePrefixes.circuit.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
    	if(gregtech.api.util.GT_OreDictUnificator.isBlacklisted(aStack)&&aModName.equals("gregtech"))return;
        switch (aMaterial.mName) {
            case "Good":
            case "Data":
            case "Elite":
            case "Master":
            case "Ultimate":
                if (!gregtech.api.util.GT_OreDictUnificator.isBlacklisted(aStack)&&!aModName.equals("gregtech"))
                    GT_ModHandler.removeRecipeByOutput(aStack);
                break;
            case "Primitive":
                GT_ModHandler.removeRecipeByOutput(aStack);
               break;
            case "Basic":
                GT_ModHandler.removeRecipeByOutput(aStack);
                GT_ModHandler.addCraftingRecipe(aStack, new Object[]{"RIR","VBV","CCC",'R',ItemList.Circuit_Parts_Resistor.get(1,new Object[0]),'C',GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.RedAlloy, 1),'V', ItemList.Circuit_Parts_Vacuum_Tube.get(1,new Object[0]),'B',ItemList.Circuit_Board_Coated.get(1,new Object[0]),'I',ItemList.IC2_Item_Casing_Steel.get(1,new Object[0])});
                GT_ModHandler.addShapelessCraftingRecipe(ItemList.Circuit_Basic.get(1L, new Object[0]), new Object[]{ItemList.Circuit_Integrated.getWildcard(1L, new Object[0])});
               break;
            case "Advanced":
                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_Transistor.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},Materials.SolderingAlloy.getMolten(72), aStack, 800, 28);
                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_TransistorSMD.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},Materials.SolderingAlloy.getMolten(72), aStack, 800, 28);
                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_Transistor.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},Materials.Tin.getMolten(144), aStack, 800, 28);
                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_TransistorSMD.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},Materials.Tin.getMolten(144), aStack, 800, 28);
                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_Transistor.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},Materials.Lead.getMolten(288), aStack, 800, 28);
                GT_Values.RA.addCircuitAssemblerRecipe(new ItemStack[]{ItemList.Circuit_Integrated_Good.get(2,new Object[0]),ItemList.Circuit_Chip_ILC.get(3,new Object[0]),ItemList.Circuit_Chip_Ram.get(1,new Object[0]),ItemList.Circuit_Parts_TransistorSMD.get(4,new Object[0]),GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 16)},Materials.Lead.getMolten(288), aStack, 800, 28);
               break;
        }
    }
}
