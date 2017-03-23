/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package biomesoplenty.common.handler;

import biomesoplenty.api.achievement.BOPAchievements;
import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPFlowers;
import biomesoplenty.api.enums.BOPPlants;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.*;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import java.util.Iterator;
import java.util.Set;

public class AchievementEventHandler 
{
    private static final Set<Biome> BOP_BIOMES_TO_EXPLORE = Sets.union(BOPBiomes.REG_INSTANCE.getPresentBiomes(), Biome.EXPLORATION_BIOMES_LIST);

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        ItemStack stack = event.pickedUp.getEntityItem();
        Item item = stack.getItem();

        Block block = Block.getBlockFromItem(item);
        EntityPlayer player = event.player;

        if (block instanceof IBOPBlock)
        {
            IBlockState state = block != null && item instanceof ItemBlock ? block.getStateFromMeta(item.getMetadata(stack.getMetadata())) : null;

            if (block instanceof BlockBOPLog)
            {
                player.addStat(AchievementList.MINE_WOOD);
            }

            //Totally Coral Achievement
            if (block != null && block == BOPBlocks.coral)
            {
                player.addStat(BOPAchievements.obtain_coral);
            }

            //Life Finds a Way Achievement
            if (block != null && state == BlockBOPFlower.paging.getVariantState(BOPFlowers.MINERS_DELIGHT))
            {
                player.addStat(BOPAchievements.obtain_miners_delight);
            }


            //Rather Thorny Achievement
            if (block != null && state == BlockBOPPlant.paging.getVariantState(BOPPlants.THORN))
            {
                player.addStat(BOPAchievements.obtain_thorn);
            }

            //I am Become Death Achievement
            if (block != null && state == BlockBOPFlower.paging.getVariantState(BOPFlowers.DEATHBLOOM))
            {
                player.addStat(BOPAchievements.obtain_deathbloom);
            }

            //Godsend Achievement
            if (block != null && state == BlockBOPFlower.paging.getVariantState(BOPFlowers.WILTED_LILY))
            {
                player.addStat(BOPAchievements.obtain_wilted_lily);
            }
        }

        //Flower Child Achievement
        if (block != null && block instanceof BlockBOPFlower || block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER)
        {
            player.addStat(BOPAchievements.obtain_flowers);
        }

        //Berry Good Achievement
        if (item != null && item == BOPItems.berries)
        {
            player.addStat(BOPAchievements.obtain_berry);
        }

        //Stalk Market Achievement
        if (item != null && item == BOPItems.turnip)
        {
            player.addStat(BOPAchievements.obtain_turnip);
        }

        //Soul Searching Achievement
        if (item != null && item == BOPItems.soul)
        {
            player.addStat(BOPAchievements.obtain_soul);
        }

        //Honeycomb's Big Achievement
        if (item != null && item == BOPItems.filled_honeycomb)
        {
            player.addStat(BOPAchievements.obtain_honeycomb);
        }

        //Don't Breathe This Achievement
        if (item != null && item == BOPItems.pixie_dust)
        {
            player.addStat(BOPAchievements.obtain_pixie_dust);
        }

        //Far Out Achievement
        if (item != null && item == BOPItems.crystal_shard)
        {
            player.addStat(BOPAchievements.obtain_celestial_crystal);
        }
    }
    
    @SubscribeEvent
    public void onItemUsed(PlayerInteractEvent event)
    {
        /* TODO: 1.9 if (event.action != Action.LEFT_CLICK_BLOCK)
        {
            ItemStack stack = event.entityPlayer.getHeldItem();
            Item item = stack != null ? stack.getItem() : null;
            EntityPlayer player = event.entityPlayer;

            //Gone Home
            if (item == BOPItems.enderporter)
            {
                player.addStat(BOPAchievements.use_enderporter);
            }
        }*/
    }
    
    @SubscribeEvent
    public void onItemUsed(LivingEntityUseItemEvent.Finish event)
    {
        ItemStack stack = event.getItem();
        Item item = stack.getItem();

        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();

            //Trippin'
            if (item == BOPItems.shroompowder) {
                player.addStat(BOPAchievements.eat_shroom_powder);
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event)
    {
        EntityPlayer player = event.getPlayer();
        IBlockState state = event.getPlacedBlock();
        if (player != null && state != null && state == BlockBOPSapling.paging.getVariantState(BOPTrees.SACRED_OAK))
        {
            player.addStat(BOPAchievements.grow_sacred_oak);
        }
    }
    
    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent event)
    {
        Item item = event.crafting.getItem();
        EntityPlayer player = event.player;
        
        //Nectar of the Gods Achievement
        if (item != null && item == BOPItems.ambrosia)
        {
            player.addStat(BOPAchievements.craft_ambrosia);
        }
        
        //Flaxen Fun Achievement
        if (item != null && item == BOPItems.flax_string)
        {
            player.addStat(BOPAchievements.craft_flax_string);
        }
        
        //Getting a Downgrade Achievement
        if (item != null && item == BOPItems.mud_pickaxe)
        {
            player.addStat(BOPAchievements.craft_muddy_pickaxe);
        }
        
        //By Your Powers Combined Achievement
        if (item != null && item == BOPItems.terrestrial_artifact)
        {
            player.addStat(BOPAchievements.craft_terrestrial_artifact);
        }
        
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingUpdateEvent event)
    {
        /* TODO: 1.9 if (!event.entity.world.isRemote && event.entity instanceof EntityPlayer)
        {
            EntityPlayerMP player = (EntityPlayerMP)event.entity;

            //Check every five seconds if the player has entered a new biome, if they haven't already gotten the achievement
            if (player.ticksExisted % 20 * 5 == 0)
            {
                if (!player.getStatFile().hasAchievementUnlocked(BOPAchievements.use_biome_finder))
                {
                    this.updateBiomeRadarExplore(player);
                }
                
                if (!player.getStatFile().hasAchievementUnlocked(BOPAchievements.explore_all_biomes))
                {
                    this.updateBiomesExplored(player);
                }
            }
        }*/
    }

    private void updateBiomeRadarExplore(EntityPlayerMP player)
    {
        Biome currentBiome = player.world.getBiome(new BlockPos(MathHelper.floor(player.posX), 0, MathHelper.floor(player.posZ)));

        //Search every item in the player's main inventory for a biome radar
        for (ItemStack stack : player.inventory.mainInventory)
        {
            //If the stack is null, skip it
            if (stack == null)
                continue;
            
            if (stack.getItem() == BOPItems.biome_finder && stack.hasTagCompound() && stack.getTagCompound().hasKey("biomeIDToFind"))
            {
                int biomeIdToFind = stack.getTagCompound().getInteger("biomeIDToFind");

                //If the current biome id is the id on the radar, award the achievement and stop searching
                if (biomeIdToFind == Biome.getIdForBiome(currentBiome)) 
                {
                    player.addStat(BOPAchievements.use_biome_finder);
                    return;
                }
            }
        }
    }
    
    private void updateBiomesExplored(EntityPlayerMP player)
    {
        Biome currentBiome = player.world.getBiome(new BlockPos(MathHelper.floor(player.posX), 0, MathHelper.floor(player.posZ)));
        String biomeName = currentBiome.getBiomeName();
        //Get a list of the current explored biomes
        JsonSerializableSet exploredBiomeNames = (JsonSerializableSet)player.getStatFile().getProgress(BOPAchievements.explore_all_biomes);

        if (exploredBiomeNames == null)
        {
            //Set the stat data
            exploredBiomeNames = (JsonSerializableSet)player.getStatFile().setProgress(BOPAchievements.explore_all_biomes, new JsonSerializableSet());
        }

        //Add the current biome to the set of biomes that the player has explored
        exploredBiomeNames.add(biomeName);

        if (player.getStatFile().canUnlockAchievement(BOPAchievements.explore_all_biomes) && exploredBiomeNames.size() >= BOP_BIOMES_TO_EXPLORE.size())
        {
            //Create a copy of the set of biomes that need to be explored to fulfil the achievement
            Set<Biome> set = Sets.newHashSet(BOP_BIOMES_TO_EXPLORE);

            //Iterate over the names of all the biomes the player has explored
            for (String exploredBiomeName : exploredBiomeNames)
            {
                Iterator<Biome> iterator = set.iterator();

                //Iterate over the set of biomes required to be explored and remove those that already have been explored
                while (iterator.hasNext())
                {
                    Biome biome = (Biome)iterator.next();

                    if (biome.getBiomeName().equals(exploredBiomeName))
                    {
                        iterator.remove();
                    }
                }

                //If there are no biomes remaining in the set to be explored, then there's no point continuing
                if (set.isEmpty())
                {
                    break;
                }
            }

            //Has the player fulfilled the achievement (there are no biomes left in the set of biomes to be explored)
            if (set.isEmpty())
            {
                player.addStat(BOPAchievements.explore_all_biomes);
            }
        }
    }
}
