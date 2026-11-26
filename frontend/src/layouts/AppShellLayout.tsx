import { Link, Outlet } from 'react-router'
import { useAudio } from '../audio/AudioProvider'
import { useAuth } from '../auth/AuthContext'

export function AppShellLayout() {
  const { isPlaying, toggle } = useAudio()
  const { role } = useAuth()

  return (
    <div data-theme="light" className="min-h-[100dvh] flex flex-col px-6 py-5 gap-6">
      <header className="flex flex-wrap items-center justify-between gap-4">
        <div className="flex items-center gap-4 text-xs font-semibold">
          <Link to="/" className="text-base font-semibold tracking-tight">
            Sola
          </Link>
          <Link to="/studio" className="sub-tile rounded-full px-3.5 py-1.5">
            Phòng nghe
          </Link>
          <Link to="/discover" className="sub-tile rounded-full px-3.5 py-1.5">
            Khám phá
          </Link>
          <Link to="/dashboard" className="sub-tile rounded-full px-3.5 py-1.5">
            Góc cá nhân
          </Link>
          <Link to="/admin" className="sub-tile rounded-full px-3.5 py-1.5">
            Quản lý kênh
          </Link>
        </div>
        <div className="flex items-center gap-3 text-xs">
          <span style={{ color: 'var(--ink-muted)' }}>Vai trò: {role}</span>
          <button
            type="button"
            onClick={toggle}
            className="charcoal-pill rounded-full px-4 py-1.5 font-semibold cursor-pointer"
          >
            {isPlaying ? 'Tạm dừng' : 'Phát thử'}
          </button>
        </div>
      </header>
      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  )
}
