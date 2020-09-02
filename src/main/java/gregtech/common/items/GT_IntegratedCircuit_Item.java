package gregtech.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_Generic_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

import static gregtech.GT_Mod.GT_FML_LOGGER;

public class GT_IntegratedCircuit_Item extends GT_Generic_Item {
    private final static String aTextEmptyRow = "   ";
    public GT_IntegratedCircuit_Item() {
        super("integrated_circuit", "Programmed Circuit", "");
        setHasSubtypes(true);
        setMaxDamage(0);

        ItemList.Circuit_Integrated.set(this);


        GT_ModHandler.addShapelessCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 0L), GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{OrePrefixes.circuit.get(Materials.Basic)});
        long bits = GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE;
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 1L), bits, new Object[]{"d  ", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 2L), bits, new Object[]{" d ", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 3L), bits, new Object[]{"  d", " P ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 4L), bits, new Object[]{aTextEmptyRow, " Pd", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 5L), bits, new Object[]{aTextEmptyRow, " P ", "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 6L), bits, new Object[]{aTextEmptyRow, " P ", " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 7L), bits, new Object[]{aTextEmptyRow, " P ", "d  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 8L), bits, new Object[]{aTextEmptyRow, "dP ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});

        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 9L), bits, new Object[]{"P d", aTextEmptyRow, aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 10L), bits, new Object[]{"P  ", "  d", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 11L), bits, new Object[]{"P  ", aTextEmptyRow, "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 12L), bits, new Object[]{"P  ", aTextEmptyRow, " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 13L), bits, new Object[]{"  P", aTextEmptyRow, "  d", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 14L), bits, new Object[]{"  P", aTextEmptyRow, " d ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 15L), bits, new Object[]{"  P", aTextEmptyRow, "d  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 16L), bits, new Object[]{"  P", "d  ", aTextEmptyRow, 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 17L), bits, new Object[]{aTextEmptyRow, aTextEmptyRow, "d P", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 18L), bits, new Object[]{aTextEmptyRow, "d  ", "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 19L), bits, new Object[]{"d  ", aTextEmptyRow, "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 20L), bits, new Object[]{" d ", aTextEmptyRow, "  P", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 21L), bits, new Object[]{"d  ", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 22L), bits, new Object[]{" d ", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 23L), bits, new Object[]{"  d", aTextEmptyRow, "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
        GT_ModHandler.addCraftingRecipe(ItemList.Circuit_Integrated.getWithDamage(1L, 24L), bits, new Object[]{aTextEmptyRow, "  d", "P  ", 'P', ItemList.Circuit_Integrated.getWildcard(1L)});
    }

    private static String getModeString(int aMetaData) {
        switch ((byte) (aMetaData >>> 8)) {
            case 0:
                return "==";
            case 1:
                return "<=";
            case 2:
                return ">=";
            case 3:
                return "<";
            case 4:
                return ">";
        }
        return "";
    }

    private static String getConfigurationString(int aMetaData) {
        return getModeString(aMetaData) + " " + (byte) (aMetaData & 0xFF);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        aList.add(GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".configuration", "Configuration: ") + getConfigurationString(getDamage(aStack)));
    }

    public String getUnlocalizedName(ItemStack aStack) {
        return getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public final void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        aList.add(new ItemStack(this, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        if (GregTech_API.sPostloadFinished) {
            GT_Log.out.println("GT_Mod: Starting Item Icon Load Phase");
            GT_FML_LOGGER.info("GT_Mod: Starting Item Icon Load Phase");
            GregTech_API.sItemIcons = aIconRegister;
            try {
                for (Runnable tRunnable : GregTech_API.sGTItemIconload) {
                    tRunnable.run();
                }
            } catch (Throwable e) {e.printStackTrace(GT_Log.err);}
            GT_Log.out.println("GT_Mod: Finished Item Icon Load Phase");
            GT_FML_LOGGER.info("GT_Mod: Finished Item Icon Load Phase");
        }
    }
}
