import { create } from 'zustand';

export interface CartItem {
  produtoId: number;
  nome: string;
  preco: number;
  quantidade: number;
  observacao: string;
  imagemUrl?: string;
}

interface CartState {
  items: CartItem[];
  isOpen: boolean;
  addItem: (item: Omit<CartItem, 'quantidade' | 'observacao'>) => void;
  removeItem: (produtoId: number) => void;
  updateQuantity: (produtoId: number, quantidade: number) => void;
  updateObservacao: (produtoId: number, observacao: string) => void;
  clearCart: () => void;
  toggleCart: () => void;
  setOpen: (open: boolean) => void;
  totalItems: () => number;
  totalPrice: () => number;
}

export const useCartStore = create<CartState>((set, get) => ({
  items: [],
  isOpen: false,

  addItem: (item) => {
    const items = get().items;
    const existing = items.find((i) => i.produtoId === item.produtoId);
    if (existing) {
      set({
        items: items.map((i) =>
          i.produtoId === item.produtoId
            ? { ...i, quantidade: i.quantidade + 1 }
            : i
        ),
      });
    } else {
      set({ items: [...items, { ...item, quantidade: 1, observacao: '' }] });
    }
  },

  removeItem: (produtoId) => {
    set({ items: get().items.filter((i) => i.produtoId !== produtoId) });
  },

  updateQuantity: (produtoId, quantidade) => {
    if (quantidade <= 0) {
      get().removeItem(produtoId);
      return;
    }
    set({
      items: get().items.map((i) =>
        i.produtoId === produtoId ? { ...i, quantidade } : i
      ),
    });
  },

  updateObservacao: (produtoId, observacao) => {
    set({
      items: get().items.map((i) =>
        i.produtoId === produtoId ? { ...i, observacao } : i
      ),
    });
  },

  clearCart: () => set({ items: [] }),
  toggleCart: () => set({ isOpen: !get().isOpen }),
  setOpen: (open) => set({ isOpen: open }),
  totalItems: () => get().items.reduce((sum, i) => sum + i.quantidade, 0),
  totalPrice: () => get().items.reduce((sum, i) => sum + i.preco * i.quantidade, 0),
}));
