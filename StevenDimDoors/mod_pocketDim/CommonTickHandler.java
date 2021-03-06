package StevenDimDoors.mod_pocketDim;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
public class CommonTickHandler implements ITickHandler
{
	 Random rand= new Random();
    public int tickCount=0;
    public int tickCount2=0;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {}

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (type.equals(EnumSet.of(TickType.SERVER)))
        {
            onTickInGame();
        }
    }

    public EnumSet ticks()
    {
        return EnumSet.of(TickType.SERVER);
    }

    public String getLabel()
    {
        return null;
    }

    //replaces rifts in game that have been destroyed/have blocks placed over them. 
    private void onTickInGame()
    {
    	try
    	{
    	
    	if(tickCount>200)
    	{
    		tickCount=0;
    		int i=0;
    		
      
    		while (i<15&&dimHelper.instance.linksForRendering.size()>0&&FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
    		{
    			i++;
    			LinkData link;
    			
    			//actually gets the random rift based on the size of the list
        		link = (LinkData) dimHelper.instance.linksForRendering.get(rand.nextInt(dimHelper.instance.linksForRendering.size()));

    			
    			
    			if(link!=null)
    			{
    			
    				if(dimHelper.getWorld(link.locDimID)!=null)
    				{
    					World world=dimHelper.getWorld(link.locDimID);
    					
    					int blocktoReplace = world.getBlockId(link.locXCoord, link.locYCoord, link.locZCoord);
    					
    					if(!mod_pocketDim.blocksImmuneToRift.contains(blocktoReplace))//makes sure the rift doesnt replace a door or something
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
    	
    	
    	}
    	catch (Exception e)
    	{
    		tickCount++;
    		System.out.println("something on tick went wrong");
    	}
    	tickCount++;
    	
    	//this section regulates decay in Limbo- it records any blocks placed by the player and later progresss them through the decay cycle
    	if(tickCount2>10&&dimHelper.blocksToDecay!=null)
    	{
    		tickCount2=0;
    		if(!dimHelper.blocksToDecay.isEmpty()&&mod_pocketDim.limbo!=null)
    		{
    		
    		
    			if(dimHelper.blocksToDecay.size()>rand.nextInt(400))
    			{
    				int index = rand.nextInt(dimHelper.blocksToDecay.size());
    				Point3D point = (Point3D) dimHelper.blocksToDecay.get(index);

    				int blockID = mod_pocketDim.limbo.getBlockId(point.getX(), point.getY(), point.getZ());
    				int idToSet=Block.stone.blockID;
    				
    				if(blockID==0||blockID==mod_pocketDim.blockLimboID)
    				{
    					dimHelper.blocksToDecay.remove(index);
    				}
    				else
    				{
    					if(Block.blocksList[idToSet] instanceof BlockContainer)
    					{
    						idToSet=-1;
    						dimHelper.blocksToDecay.remove(index); 
    					}
    					
    		
    			
    					if(blockID==Block.cobblestone.blockID)
    					{
    						idToSet=Block.gravel.blockID;
    					}
    					if(blockID==Block.stone.blockID)
    					{
    						idToSet=Block.cobblestone.blockID;

    					}
    					if(blockID==Block.gravel.blockID&&!mod_pocketDim.limbo.isAirBlock(point.getX(), point.getY()-1, point.getZ()))
    					{
    						idToSet=mod_pocketDim.blockLimboID;
    					}
    					else if(blockID==Block.gravel.blockID)
    					{
    						dimHelper.blocksToDecay.remove(index);
    						idToSet=-1;

    					}
    					
    					if(idToSet!=-1)
    					{
    					
    						mod_pocketDim.limbo.setBlockWithNotify(point.getX(), point.getY(), point.getZ(), idToSet);

    					}    		
    				}   		    		
    			}   		
    		}
    	}
    	
    	tickCount2++;
    }
}