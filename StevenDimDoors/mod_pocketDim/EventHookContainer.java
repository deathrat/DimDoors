package StevenDimDoors.mod_pocketDim;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
public class EventHookContainer
{
	Random rand= new Random();
    @ForgeSubscribe
    public void onWorldLoad(WorldEvent.Load event)
    {
    	
    	if(!mod_pocketDim.hasInitDims&&event.world.provider.dimensionId==0&&!event.world.isRemote)
    	{
    		dimHelper.instance.unregsisterDims();
        	dimHelper.dimList.clear();
        	dimHelper.instance.interDimLinkList.clear();
        	dimHelper.instance.linksForRendering.clear();
        	dimHelper.instance.initPockets();
        	
        	mod_pocketDim.limbo= dimHelper.instance.getWorld(mod_pocketDim.limboDimID);
        	
    	}
    	
    	
    	
    	
    	
		Iterator itr = ((ArrayList) dimHelper.instance.linksForRendering.clone()).listIterator();
  
		while (itr.hasNext())
		{
			
			
			LinkData link = (LinkData) itr.next();
			
			if(link!=null)
			{
			
			if(dimHelper.getWorld(link.locDimID)!=null)
			{
			//	link.printLinkData();
				World world=dimHelper.getWorld(link.locDimID);
				int blocktoReplace = world.getBlockId(link.locXCoord, link.locYCoord, link.locZCoord);
				if(!mod_pocketDim.blocksImmuneToRift.contains(blocktoReplace))
				{
					if(dimHelper.instance.getLinkDataFromCoords(link.locXCoord, link.locYCoord, link.locZCoord, link.locDimID)==null)
					{
						dimHelper.instance.linksForRendering.remove(link);
					}
					else
					{
						dimHelper.getWorld(link.locDimID).setBlockWithNotify(link.locXCoord, link.locYCoord, link.locZCoord, mod_pocketDim.blockRiftID);

					}
				}
			}
			}
	   
		}
    	
    	
       
    }
    @ForgeSubscribe
	public void EntityJoinWorldEvent(net.minecraftforge.event.entity.EntityJoinWorldEvent event)
    {
    if(event.entity instanceof EntityPlayer)
    	{
    //	System.out.println(event.entity.worldObj.provider.dimensionId);

    	//	PacketDispatcher.sendPacketToPlayer(DimUpdatePacket.sendPacket(event.world.provider.dimensionId,1),(Player) event.entity);
    		
    		
    		
    	}
        
    }
    @ForgeSubscribe
    public void onPlayerFall(LivingFallEvent event)
    {
    	
    		event.setCanceled(event.entity.worldObj.provider.dimensionId==mod_pocketDim.limboDimID);
    	
    }
    
    @ForgeSubscribe
    public void onPlayerInteract(PlayerInteractEvent event)
    {
     
    
    		
    	
    	
     if(event.entityPlayer.worldObj.provider.dimensionId==mod_pocketDim.limboDimID&&event.action==PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
     {
        

         int x = event.x;
         int y = event.y;
         
         int z = event.z;
         
         
         
//need to propely separate client/server
         int face = event.face;
         switch (face) {
             case 0:  y = y-1;
                      break;
             case 1: y=y+1;
                      break;
             case 2:  z=z-1;
                      break;
             case 3:  z=z+1;
                      break;
             case 4:  x=x-1;
                      break;
             case 5:  x=x+1 ;
                      break;
             default:
                      break;
         }
         
         if(event.entityPlayer.getHeldItem()!=null)
         {
         if(event.entityPlayer.getHeldItem().getItem() instanceof ItemBlock)
         {
        //	if(event.entityPlayer instanceof EntityPlayerMP)
        	{
        	
        		Point3D point = new Point3D(x,y,z);
        		dimHelper.blocksToDecay.add(point);
        	}
         }
         else
         {
        	 event.setCanceled(true);
         }
         
         
         }
     }
     
    	}
     
     
    
    
 //   @ForgeSubscribe
    public void onPlayerEvent(PlayerEvent event)
    {
    	/**
       if(!event.entity.worldObj.isRemote)
       {
    	   ItemStack item =  event.entityPlayer.inventory.getCurrentItem();
    	   if(item!=null)
    	   {
    		   if(item.getItem() instanceof ItemRiftBlade)
    		   {
					List<EntityLiving> list =  event.entity.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox( event.entityPlayer.posX-7,event.entityPlayer.posY-7, event.entityPlayer.posZ-7, event.entityPlayer.posX+7,event.entityPlayer.posY+7, event.entityPlayer.posZ+7));
					list.remove(event.entity);
	    			  

					for(EntityLiving ent : list)
					{
						
						  Vec3 var3 = event.entityPlayer.getLook(1.0F).normalize();
				            Vec3 var4 =  event.entityPlayer.worldObj.getWorldVec3Pool().getVecFromPool(ent.posX -  event.entityPlayer.posX, ent.boundingBox.minY + (double)((ent.height) / 2.0F) - ( event.entityPlayer.posY + (double) event.entityPlayer.getEyeHeight()), ent.posZ -  event.entityPlayer.posZ);
				            double var5 = var4.lengthVector();
				            var4 = var4.normalize();
				            double var7 = var3.dotProduct(var4);
				            if( (var7+.1) > 1.0D - 0.025D / var5 ?  event.entityPlayer.canEntityBeSeen(ent) : false)
				            {
				            	 System.out.println(list.size());
				            	ItemRiftBlade.class.cast(item.getItem()).teleportToEntity(item,ent, event.entityPlayer);
				            	break;

				            	//ItemRiftBlade.class.cast(item.getItem()).teleportTo(event.entityPlayer, ent.posX, ent.posY, ent.posZ);
				            }
					}
    			
    		   }
    	   }
       }
       **/
    }
    @ForgeSubscribe
    public void onPlayerDrops(PlayerDropsEvent event)
    {
    	mod_pocketDim.limboSpawnInventory=event.drops;
    }

    @ForgeSubscribe
    public void onWorldunload(WorldEvent.Unload event)
    {
     
    	
    }

    @ForgeSubscribe
    public void onWorldsave(WorldEvent.Save event)
    {
    
    	if(mod_pocketDim.hasInitDims&&event.world.provider.dimensionId==0)
    	{
    		dimHelper.instance.save();
    	}
    }
}