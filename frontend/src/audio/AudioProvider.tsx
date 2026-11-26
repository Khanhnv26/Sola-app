import { createContext, useContext, useState, type ReactNode } from 'react'

interface AudioContextValue {
  isPlaying: boolean
  toggle: () => void
}

const AudioContext = createContext<AudioContextValue | null>(null)

export function AudioProvider({ children }: { children: ReactNode }) {
  const [isPlaying, setIsPlaying] = useState(false)

  const toggle = () => {
    setIsPlaying((prev) => !prev)
  }

  return (
    <AudioContext.Provider value={{ isPlaying, toggle }}>
      {children}
    </AudioContext.Provider>
  )
}

export function useAudio(): AudioContextValue {
  const ctx = useContext(AudioContext)
  if (!ctx) {
    throw new Error('useAudio phải được gọi bên trong AudioProvider')
  }
  return ctx
}
