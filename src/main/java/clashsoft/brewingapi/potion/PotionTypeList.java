package clashsoft.brewingapi.potion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.brewingapi.potion.type.PotionType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;

public class PotionTypeList extends ArrayList<IPotionType>
{
	protected ItemStack		stack;
	protected NBTTagList	tagList;
	
	public static PotionTypeList create(ItemStack stack)
	{
		boolean flag = false;
		if (stack.stackTagCompound == null)
		{
			flag = true;
			stack.stackTagCompound = new NBTTagCompound();
		}
		
		NBTTagList tagList = (NBTTagList) stack.stackTagCompound.getTag(IPotionType.COMPOUND_NAME);
		if (tagList == null)
		{
			tagList = new NBTTagList();
			stack.stackTagCompound.setTag(IPotionType.COMPOUND_NAME, tagList);
		}
		
		PotionTypeList list = new PotionTypeList(stack, tagList);
		
		if (flag)
		{
			List<PotionEffect> effects = ((ItemPotion2) stack.getItem()).getSuperEffects(stack);
			
			if (effects != null && !effects.isEmpty())
			{
				for (PotionEffect effect : effects)
				{
					IPotionType potionType = PotionType.getFromEffect(effect);
					if (potionType != null)
					{
						list.add(potionType);
					}
				}
			}
		}
		
		return list;
	}
	
	private PotionTypeList(ItemStack stack, NBTTagList tagList)
	{
		super(tagList.tagCount());
		this.addAll(tagList);
		this.tagList = tagList;
		this.stack = stack;
	}
	
	public void addAll(NBTTagList tagList)
	{
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound compound = tagList.getCompoundTagAt(i);
			IPotionType type = PotionType.getFromNBT(compound);
			if (type != null)
			{
				super.add(type);
			}
		}
	}
	
	private static NBTBase toNBT(IPotionType e)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		e.writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public boolean add(IPotionType e)
	{
		if (this.tagList != null)
		{
			this.tagList.appendTag(toNBT(e));
		}
		return super.add(e);
	}
	
	@Override
	public IPotionType set(int index, IPotionType e)
	{
		if (this.tagList != null)
		{
			this.tagList.func_150304_a(index, toNBT(e));
		}
		return super.set(index, e);
	}
	
	@Override
	public void clear()
	{
		if (this.tagList != null)
		{
			int tagCount = this.tagList.tagCount();
			while (tagCount > 0)
			{
				this.tagList.removeTag(--tagCount);
			}
		}
		super.clear();
	}
	
	@Override
	public boolean addAll(Collection<? extends IPotionType> c)
	{
		if (this.tagList != null)
		{
			for (IPotionType e : c)
			{
				this.tagList.appendTag(toNBT(e));
			}
		}
		return super.addAll(c);
	}
	
	@Override
	public IPotionType remove(int index)
	{
		if (this.tagList != null)
		{
			this.tagList.removeTag(index);
		}
		return super.remove(index);
	}
	
	@Override
	public boolean remove(Object o)
	{
		if (o instanceof IPotionType)
		{
			int i = this.indexOf((IPotionType) o);
			if (i != -1)
			{
				return this.remove(i) != null;
			}
		}
		return super.remove(o);
	}
}
