import { Link, Outlet } from 'react-router'

export function PublicLayout() {
  return (
    <div data-theme="light" className="min-h-[100dvh] flex flex-col px-6 py-5 gap-6">
      <header className="flex items-center justify-between">
        <Link to="/" className="text-base font-semibold tracking-tight">
          Sola
        </Link>
        <nav className="flex items-center gap-3 text-xs font-semibold">
          <Link to="/studio" className="sub-tile rounded-full px-4 py-2">
            Nghe thử
          </Link>
          <Link to="/login" className="charcoal-pill rounded-full px-4 py-2">
            Đăng nhập
          </Link>
        </nav>
      </header>
      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  )
}
