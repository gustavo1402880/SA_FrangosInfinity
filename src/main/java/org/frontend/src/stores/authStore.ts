import { create } from 'zustand';
import api from '@/lib/api';

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  telefone?: string;
  tipoUsuario: 'CLIENTE' | 'FUNCIONARIO';
  nivelAcesso?: 'ADMINISTRADOR' | 'ATENDENTE' | 'COZINHEIRO' | 'CAIXA';
  ativo: boolean;
  matricula?: string;
  sucesso?: boolean;
  mensagem?: string;
}

interface AuthState {
  user: Usuario | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  hasChecked: boolean;
  error: string | null;
  login: (email: string, senha: string) => Promise<boolean>;
  logout: () => Promise<void>;
  checkAuth: () => Promise<void>;
  clearError: () => void;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isAuthenticated: false,
  isLoading: false,
  hasChecked: false,
  error: null,

  login: async (email: string, senha: string) => {
    set({ isLoading: true, error: null });
    try {
      const formData = new URLSearchParams();
      formData.append('email', email);
      formData.append('senha', senha);

      await api.post('/login', formData, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      });

      const { data } = await api.get<Usuario>('/auth/me');
      set({ user: data, isAuthenticated: true, isLoading: false, hasChecked: true });
      return true;
    } catch (err: any) {
      set({
        error: err.response?.status === 401
          ? 'Email ou senha inválidos'
          : 'Erro ao fazer login. Tente novamente.',
        isLoading: false,
        hasChecked: true,
      });
      return false;
    }
  },

  logout: async () => {
    try {
      await api.post('/auth/logout');
    } catch {
      // ignore
    }
    set({ user: null, isAuthenticated: false, isLoading: false, hasChecked: true });
  },

  checkAuth: async () => {
    if (get().hasChecked && get().isAuthenticated) return;
    set({ isLoading: true });
    try {
      const { data: isAuth } = await api.get<boolean>('/auth/status');
      if (isAuth) {
        const { data } = await api.get<Usuario>('/auth/me');
        set({ user: data, isAuthenticated: true, isLoading: false, hasChecked: true });
      } else {
        set({ user: null, isAuthenticated: false, isLoading: false, hasChecked: true });
      }
    } catch {
      set({ user: null, isAuthenticated: false, isLoading: false, hasChecked: true });
    }
  },

  clearError: () => set({ error: null }),
}));
