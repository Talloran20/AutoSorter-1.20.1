package talloran.autosorter.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import talloran.autosorter.custom.blockentity.AutoSorterBlockEntity;

public class AutoSorterScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final AutoSorterBlockEntity blockEntity;

    private ButtonWidget button1;

    public AutoSorterScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(0));
    }

    public AutoSorterScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate PropertyDelegate) {
        super(ModScreenHandler.AUTO_SORTER_SCREEN_HANDLER, syncId);
        checkSize(((Inventory)blockEntity), 56);
        this.inventory = ((Inventory)blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = PropertyDelegate;
        this.blockEntity = ((AutoSorterBlockEntity) blockEntity);


        //инвентарь фильтров
        for (int i = 0; i < 6; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new filterSlot(inventory, l + i * 9, 48 + l * 18, i * 18 + 18));
            }
        }


        //два слота для частоты
        this.addSlot(new filterSlot(inventory, 54, 217, 18));
        this.addSlot(new filterSlot(inventory, 55, 235, 18));



        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(PropertyDelegate);
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }



    //быстрое перемешение предметов передвигается сразу стак,
    // а должен только 1 предмет из стака.
    // Кто знает как пофиксить - можете пофиксить, буду благодарен
    @Override
    public ItemStack quickMove(PlayerEntity player, int Slot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(Slot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (Slot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
        }


    //добавление инвенторя игрока  (индекс от 9 до 35)
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 48 + l * 18, 140 + i * 18));
            }
        }
    }

    //добавление хотбара игрока  (индекс от 0 до 8)
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 48 + i * 18, 198));
        }
    }


    class filterSlot extends Slot {
        public filterSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }
    }
}

