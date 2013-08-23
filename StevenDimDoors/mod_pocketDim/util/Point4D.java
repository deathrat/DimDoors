package StevenDimDoors.mod_pocketDim.util;

public final class Point4D
{
	private final int x;
	private final int y;
	private final int z;
	private final int dimension;
	
	public Point4D(int x, int y, int z, int dimension)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}

	public int getDimension()
	{
		return dimension;
	}
	
	@Override
	public int hashCode()
	{
		//Time for some witchcraft.
		//The code here is inspired by a discussion on Stack Overflow regarding hash codes for 3D.
		//Source: http://stackoverflow.com/questions/9858376/hashcode-for-3d-integer-coordinates-with-high-spatial-coherence
		
		//I believe that most of the time, any points we might be hashing will be in close proximity to each other.
		//For instance, points that are within the same chunk or within a few neighboring chunks. Only the low-order
		//bits of each component would differ. I'll use 8 bits from Y and the 12 bits from X and Z. ~SenseiKiwi
		
		int bit;
		int hash;
		int index;
		
		hash = 0;
		index = 0;
		for (bit = 0; bit < 8; bit++)
		{
			hash |= ((y >> bit) & 1) << index;
			index++;
			hash |= ((x >> bit) & 1) << index;
			index++;
			hash |= ((z >> bit) & 1) << index;
			index++;
		}
		for (; bit < 12; bit++)
		{
			hash |= ((x >> bit) & 1) << index;
			index++;
			hash |= ((z >> bit) & 1) << index;
			index++;
		}
		return hash;
	}
	
	public long longHashCode()
	{
		//Time for some witchcraft.
		//The code here is inspired by a discussion on Stack Overflow regarding hash codes for 3D.
		//Source: http://stackoverflow.com/questions/9858376/hashcode-for-3d-integer-coordinates-with-high-spatial-coherence
		
		//Use 8 bits from Y and 24 bits from X and Z. Mix in 8 bits from the destination dim ID too - that means
		//even if you aligned two doors perfectly between two pockets, it's unlikely they would lead to the same dungeon.
		
		int bit;
		int index;
		long hash;
		
		hash = 0;
		index = 0;
		for (bit = 0; bit < 8; bit++)
		{
			hash |= ((dimension >> bit) & 1) << index;
			index++;
			hash |= ((x >> bit) & 1) << index;
			index++;
			hash |= ((y >> bit) & 1) << index;
			index++;
			hash |= ((z >> bit) & 1) << index;
			index++;
		}
		for (; bit < 24; bit++)
		{
			hash |= ((x >> bit) & 1) << index;
			index++;
			hash |= ((z >> bit) & 1) << index;
			index++;
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		return equals((Point4D) obj);
	}
	
	public boolean equals(Point4D other)
	{
		if (this == other)
			return true;
		if (other == null)
			return false;
		
		return (x == other.x && y == other.y && z == other.z && dimension == other.dimension);
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ", " + dimension + ")";
	}
}
