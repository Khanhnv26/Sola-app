import { createContext, useContext, type ReactNode } from 'react'

export type UserRole = 'GUEST' | 'USER' | 'ADMIN'

interface AuthContextValue {
  role: UserRole
  displayName: string
}

const AuthContext = createContext<AuthContextValue>({
  role: 'GUEST',
  displayName: 'Bạn',
})

export function AuthProvider({ children }: { children: ReactNode }) {
  return (
    <AuthContext.Provider value={{ role: 'GUEST', displayName: 'Bạn' }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextValue {
  return useContext(AuthContext)
}
