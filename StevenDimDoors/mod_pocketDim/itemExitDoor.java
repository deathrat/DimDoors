package StevenDimDoors.mod_pocketDim;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class itemExitDoor extends itemDimDoor
{
    private Material doorMaterial;

    public itemExitDoor(int par1, Material par2Material)
    {
    	  super(par1, par2Material);
          this.doorMaterial = par2Material;
          this.setCreativeTab(CreativeTabs.tabTransport);

          //  this.setIconIndex(Item.doorWood.getIconFromDamage(0));
    }
    
    @Override
	 public String getTextureFile()
	 {
		 return "/PocketBlockTextures.png";
	 }
    
    
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	
    
    		par3List.add("Place on the block under a rift");
    		par3List.add ("in any dimension,");
    		par3List.add("or place anywhere in pocket dim");
    		par3List.add("to approach surface");


    	
    }
    

}