package talloran.autosorter.custom.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import talloran.autosorter.Autosorter;

import java.util.List;
public class MainAutoSorterBlockEntity extends BlockEntity {
    private static int tickInt;
    protected final PropertyDelegate propertyDelegate;
    public MainAutoSorterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.MAIN_AUTO_SORTER_BLOCK_ENTITY, pos, state);


        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    @Nullable
    private static Inventory getInventoryAt(World world, double x, double y, double z) {
        List<Entity> list;
        BlockEntity blockEntity;
        Inventory inventory = null;
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)((Object)block)).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(blockPos)) instanceof Inventory && (inventory = (Inventory)((Object)blockEntity)) instanceof ChestBlockEntity && block instanceof ChestBlock) {
            inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
        }
        if (inventory == null && !(list = world.getOtherEntities(null, new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntityPredicates.VALID_INVENTORIES)).isEmpty()) {
            inventory = (Inventory)((Object)list.get(world.random.nextInt(list.size())));
        }
        return inventory;
    }



    public static void tick(World world, BlockPos pos, BlockState state, MainAutoSorterBlockEntity blockEntity) {
        if(world.isClient) {
            return;
        }

        tickInt++;
        if(tickInt > 4){   //выполняется каждые 5 тиков
            tickInt = 0;
            Inventory MainInventory = getInventoryAt(world, pos, state);
            if(MainInventory == null){
                return;
            }
            if(state.get(Properties.POWERED) && !MainInventory.isEmpty()){  //(если запитан редстоуном и не пустой)
                ItemStack itemStack = blockEntity.getMyItemStack(MainInventory);
                if (itemStack != ItemStack.EMPTY){
                    AutoSorterBlockEntity.MainSorterItemStack = itemStack;
                }
            }
        }
    }


    // взять стак предметов
    private ItemStack getMyItemStack(Inventory MainInventory) {
        ItemStack itemStack = null;
        for (int i = 0; i < MainInventory.size(); ++i){
            itemStack = MainInventory.getStack(i);
            if(AutoSorterBlockEntity.FirstSorter(itemStack)){
                MainInventory.setStack(i, ItemStack.EMPTY);
                return itemStack;
            }
        }
        return itemStack;
    }


    @Nullable // первичная функция поиска инвенторя
    public static Inventory getInventoryAt(World world, BlockPos pos, BlockState state) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case EAST ->
                    MainAutoSorterBlockEntity.getInventoryAt(world, (double) pos.getX() - 1, (double) pos.getY(), (double) pos.getZ());
            case SOUTH ->
                    MainAutoSorterBlockEntity.getInventoryAt(world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ() - 1);
            case WEST ->
                    MainAutoSorterBlockEntity.getInventoryAt(world, (double) pos.getX() + 1, (double) pos.getY(), (double) pos.getZ());
            default ->
                    MainAutoSorterBlockEntity.getInventoryAt(world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ() + 1);
        };
    }


}
