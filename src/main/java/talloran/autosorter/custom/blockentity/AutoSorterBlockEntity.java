package talloran.autosorter.custom.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import talloran.autosorter.Autosorter;
import talloran.autosorter.screen.AutoSorterScreenHandler;


import java.util.List;

public class AutoSorterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    public static ItemStack MainSorterItemStack = ItemStack.EMPTY;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(56, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    public AutoSorterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.AUTO_SORTER_BLOCK_ENTITY, pos, state);


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
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Auto Sorter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AutoSorterScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }


    //--------------------------------------------------------------------


    // медот выполняется каждый тик
    private static int tickInt;
    public void tick(World world, BlockPos pos, BlockState state, AutoSorterBlockEntity blockEntity) {
        if (world.isClient) {
           return;
        }
        tickInt++;
        if (tickInt > 39){
            tickInt = 0;
            MainAutoSorterBlockEntity.AddSorterItems(blockEntity.inventory);
            //Autosorter.LOGGER.info("\n" + MainAutoSorterBlockEntity.SorterItems + "\n");
        }


        if (MainSorterItemStack.getItem() != Items.AIR) {
            if (blockEntity.EmplySlot(world, pos, state)){
                Sorter(MainSorterItemStack, world, pos, state, blockEntity);
            }
        }

    }

    //есть ли пустые слоты
    public boolean EmplySlot(World world, BlockPos pos, BlockState state){
        Inventory inventory = getInventoryAt(world, pos, state);
        if (inventory != null){
            for (int i = 0; i < inventory.size(); ++i){
                if (inventory.getStack(i).isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }


    // поиск совпадений
    private static void Sorter(ItemStack stack, World world, BlockPos pos, BlockState state, AutoSorterBlockEntity blockEntity) {
        ItemStack itemStack = stack.copy();
        for (int i = 0; i < blockEntity.inventory.size(); ++i) {
            if (itemStack.getItem() == blockEntity.inventory.get(i).getItem()) {
                MainSorterItemStack = ItemStack.EMPTY;
                Insert(itemStack, world, pos, state, blockEntity);
                break;
            }
        }
    }


    // переместить предмет в найденный инвентарь
    private static void Insert(ItemStack itemStack, World world, BlockPos pos, BlockState state, AutoSorterBlockEntity blockEntity){
        Inventory inventory1 = getInventoryAt(world, pos, state);

        if (inventory1 != null){
            for (int e = 0; e < inventory1.size(); ++e){
                if (inventory1.getStack(e).isEmpty()){
                    inventory1.setStack(e, itemStack);
                    break;
                }
                else if(blockEntity.canMergeItems(inventory1.getStack(e), itemStack)){
                    ItemStack stack = ItemTransfer(inventory1.getStack(e), itemStack);
                    inventory1.setStack(e, stack);
                    break;
                }
            }
            inventory1.markDirty();
        }
    }

    private static ItemStack ItemTransfer(ItemStack itemStack, ItemStack stack) {

        int i = stack.getMaxCount() - itemStack.getCount();
        int j = Math.min(stack.getCount(), i);
        stack.decrement(j);
        itemStack.increment(j);

        return itemStack;
    }

    private boolean canMergeItems(ItemStack first, ItemStack second) {
        return first.getCount() < first.getMaxCount() && ItemStack.canCombine(first, second);
    }


    @Nullable // поиск инвенторя
    private static Inventory getInventoryAt(World world, double x, double y, double z) {
        List<Entity> list;
        BlockEntity blockEntity;
        Inventory inventory = null;
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)(block)).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(blockPos)) instanceof Inventory && (inventory = (Inventory)(blockEntity)) instanceof ChestBlockEntity && block instanceof ChestBlock) {
            inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
        }
        if (inventory == null && !(list = world.getOtherEntities(null, new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntityPredicates.VALID_INVENTORIES)).isEmpty()) {
            inventory = (Inventory)(list.get(world.random.nextInt(list.size())));
        }
        return inventory;
    }

    @Nullable // определение какую координату изменять в зависимости от направления
    public static Inventory getInventoryAt(World world, BlockPos pos, BlockState state) {
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case EAST ->
                    AutoSorterBlockEntity.getInventoryAt(world, (double) pos.getX() + 1, pos.getY(), pos.getZ());
            case SOUTH ->
                    AutoSorterBlockEntity.getInventoryAt(world, pos.getX(), pos.getY(), (double) pos.getZ() + 1);
            case WEST ->
                    AutoSorterBlockEntity.getInventoryAt(world, (double) pos.getX() - 1, pos.getY(), pos.getZ());
            default ->
                    AutoSorterBlockEntity.getInventoryAt(world, pos.getX(), pos.getY(), (double) pos.getZ() - 1);
        };
    }

}


