package StevenDimDoors.mod_pocketDim;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockLimbo extends Block
{
	
	protected BlockLimbo(int i, int j, Material par2Material) 
	{
		 super(i, j, Material.ground);
	        setTickRandomly(false);
	        this.setCreativeTab(CreativeTabs.tabBlock);
	        this.setTextureFile("/PocketBlockTextures.png");
	       
	       
	        
	}
	
	
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {}
    
    //part of the decay mech, if a block has fallen onto it, when it turns, it makes sure any block above it gets added too. 
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
    	if(par1World.getBlockId(par2, par3+1, par4)==Block.gravel.blockID)
    	{
    		 Point3D point = new Point3D(par2,par3+1,par4);
             dimHelper.blocksToDecay.add(point);
    	}
    }
    public int quantityDropped(Random par1Random)
    {
        
        
            return 1;
        
    }
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9)
    {
       
	return false;

    }
    //if a block lands on it and its gravel, adds it to the decay list
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) 
    {
    	
    	if(par1World.getBlockId(par2, par3+1, par4)==Block.gravel.blockID)
    	{
    		 Point3D point = new Point3D(par2,par3+1,par4);
             dimHelper.blocksToDecay.add(point);
    	}
    	
    }

   
   //TODO set render color!!
}
