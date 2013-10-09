package StevenDimDoors.mod_pocketDim.commands;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;
import StevenDimDoors.mod_pocketDim.core.DimLink;
import StevenDimDoors.mod_pocketDim.core.LinkTypes;
import StevenDimDoors.mod_pocketDim.core.NewDimData;
import StevenDimDoors.mod_pocketDim.core.PocketManager;

public class CommandResetDungeons extends DDCommandBase
{	
	private static CommandResetDungeons instance = null;
	
	private CommandResetDungeons()
	{
		super("dd-resetdungeons", "");
	}
	
	public static CommandResetDungeons instance()
	{
		if (instance == null)
			instance = new CommandResetDungeons();
		
		return instance;
	}

	@Override
	protected DDCommandResult processCommand(EntityPlayer sender, String[] command)
	{
		if(sender.worldObj.isRemote)
		{
			return DDCommandResult.SUCCESS; 
		}
		if (command.length > 0)
		{
			return DDCommandResult.TOO_FEW_ARGUMENTS;
		}
		
		int dungeonCount = 0;
		int resetCount = 0;
		ArrayList<Integer> dimsToDelete = new ArrayList<Integer>();
		for (NewDimData data : PocketManager.getDimensions())
		{
			
			if(DimensionManager.getWorld(data.id())==null&&data.isDungeon())
			{
				resetCount++;
				dungeonCount++;
				dimsToDelete.add(data.id());
			}
			else if(data.isDungeon())
			{
				data.setParentToRoot();
				dungeonCount++;
				for(DimLink link : data.links())
				{
					if(link.linkType()==LinkTypes.REVERSE)
					{
						data.createLink(link.source(), LinkTypes.DUNGEON_EXIT, link.orientation());
					}
					if(link.linkType()==LinkTypes.DUNGEON)
					{
						data.createLink(link.source(), LinkTypes.DUNGEON, link.orientation());
					}
				}
			}
		}
		NewDimData test = PocketManager.getDimensionData(sender.worldObj.provider.dimensionId);
		test.parent().depth();
		for(Integer dimID:dimsToDelete)
		{
			PocketManager.deletePocket(PocketManager.getDimensionData(dimID), true);
		}
		test.parent().depth();
		//TODO- for some reason test.parent is null even though I just set like 23 lines earlier, in data.setParentToRoot
		//TODO implement blackList
		//Notify the user of the results
		sender.sendChatToPlayer("Reset complete. " + resetCount + " out of " + dungeonCount + " dungeons were reset.");
		return DDCommandResult.SUCCESS;
	}
}