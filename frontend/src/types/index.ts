export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  category: string;
  categoryDisplayName: string;
}

export interface CartItem {
  product: Product;
  quantity: number;
}