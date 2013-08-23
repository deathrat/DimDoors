package StevenDimDoors.mod_pocketDim.items;

import java.util.List;

import StevenDimDoors.mod_pocketDim.SchematicLoader;
import StevenDimDoors.mod_pocketDim.Spells;
import StevenDimDoors.mod_pocketDim.mod_pocketDim;
import StevenDimDoors.mod_pocketDim.core.NewLinkData;
import StevenDimDoors.mod_pocketDim.helpers.dimHelper;
import StevenDimDoors.mod_pocketDim.ticking.CommonTickHandler;
import StevenDimDoors.mod_pocketDimClient.ClientTickHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.RotationHelper;

public class ItemStableFabric extends Item
{
	private Material doorMaterial;

	public ItemStableFabric(int par1, int par2)
	{
		super(par1);
		//  this.setitemIcon(Item.doorWood.getIconFromDamage(0));
        this.setCreativeTab(mod_pocketDim.dimDoorsCreativeTab);

	}
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(mod_pocketDim.modid + ":" + this.getUnlocalizedName().replace("item.", ""));

	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{

		if(!par3World.isRemote)
		{
			System.out.println("Block metadata is "+par3World.getBlockMetadata(par4, par5, par6));
			System.out.println(par3World.getBiomeGenForCoords(par4, par6).biomeName);


			this.onItemRightClick(par1ItemStack, par3World, par2EntityPlayer);

			Block block = Block.blocksList[par3World.getBlockId(par4, par5, par6)];

			if(dimHelper.dimList.containsKey(par3World.provider.dimensionId))
			{
				if(dimHelper.instance.getDimData(par3World.provider.dimensionId).isPocket)
				{
					if(dimHelper.instance.getDimData(par3World.provider.dimensionId).dungeonGenerator!=null)
					{
						System.out.println("Dungeon name "+dimHelper.instance.getDimData(par3World.provider.dimensionId).dungeonGenerator.schematicPath);

					}
				}

			}




		}
		//System.out.println("Block texture data is "+Block.blocksList[par3World.getBlockId(par4, par5, par6)].getBlockTexture(par3World,par4, par5, par6,par7).getIconName());
		//System.out.println("Block name is is "+Block.blocksList[par3World.getBlockId(par4, par5, par6)].getUnlocalizedName2());

		return true;
	}


	public MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
	{
		float var4 = 1.0F;
		float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
		float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
		double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)var4;
		double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par2EntityPlayer.yOffset;
		double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)var4;
		Vec3 var13 = par1World.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
		float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
		float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		float var17 = MathHelper.sin(-var5 * 0.017453292F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = 5.0D;
		if (par2EntityPlayer instanceof EntityPlayerMP)
		{
			var21 = 4;
		}
		Vec3 var23 = var13.addVector((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
		return par1World.rayTraceBlocks_do_do(var13, var23, true, false);
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (this.isSteven(par3EntityPlayer))
		{
			new Spells(par3EntityPlayer, par1ItemStack.stackSize).cast();
			//mod_pocketDim.proxy.startCasting(par3EntityPlayer, par1ItemStack.stackSize);
		}
		
		Boolean didFindThing=false;
		MovingObjectPosition hit = 	this.getMovingObjectPositionFromPlayer(par3EntityPlayer.worldObj, par3EntityPlayer, false );
		if(hit!=null&&!par2World.isRemote)
		{
			//if(par2World.getBlockId(hit.blockX, hit.blockY, hit.blockZ)==properties.RiftBlockID)
			{
				NewLinkData link = dimHelper.instance.getLinkDataFromCoords(hit.blockX, hit.blockY, hit.blockZ, par2World);
				if(link!=null)
				{
					Block var11;
					if(par1ItemStack.getItem() instanceof itemExitDoor )
					{
						var11 = mod_pocketDim.ExitDoor;
					}

					else if(par1ItemStack.getItem() instanceof ItemChaosDoor )
					{
						var11 = mod_pocketDim.chaosDoor;
					}
					else
					{
						var11 = mod_pocketDim.dimDoor;
					}

					int par4 = hit.blockX;
					int par5 = hit.blockY-1;
					int par6 = hit.blockZ;
					int par7 = 0 ;




					if (par3EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par3EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack)&&!par2World.isRemote)
					{
						int var12 = MathHelper.floor_double((double)((par3EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
						String cardinal= "default";

						switch(link.linkOrientation)
						{
						case 0:
							cardinal = "East";
							break;
						case 1:
							cardinal = "South";
							break;
						case 2:
							cardinal = "West";
							break;
						case 3:
							cardinal = "North";
							break;
						}
						System.out.println("Link orientation is " + link.linkOrientation + " - " + cardinal);
					}
				}
			}
		}
		return par1ItemStack;
	}


	public boolean isSteven(EntityPlayer player)
	{
		return (player.username.equalsIgnoreCase("stevenrs11"));
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{

	}
}
