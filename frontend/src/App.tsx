import { BrowserRouter, Route, Routes } from 'react-router'
import { AudioProvider } from './audio/AudioProvider'
import { AuthProvider } from './auth/AuthContext'
import { AppShellLayout } from './layouts/AppShellLayout'
import { PublicLayout } from './layouts/PublicLayout'
import { AdminChannelPage } from './pages/AdminChannelPage'
import { DiscoverPage } from './pages/DiscoverPage'
import { LandingPage } from './pages/LandingPage'
import { LoginPage } from './pages/LoginPage'
import { StudioPage } from './pages/StudioPage'
import { UserDashboardPage } from './pages/UserDashboardPage'

export function App() {
  return (
    <AuthProvider>
      <AudioProvider>
        <BrowserRouter>
          <Routes>
            <Route element={<PublicLayout />}>
              <Route path="/" element={<LandingPage />} />
              <Route path="/login" element={<LoginPage />} />
            </Route>
            <Route element={<AppShellLayout />}>
              <Route path="/studio" element={<StudioPage />} />
              <Route path="/discover" element={<DiscoverPage />} />
              <Route path="/dashboard" element={<UserDashboardPage />} />
              <Route path="/admin" element={<AdminChannelPage />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </AudioProvider>
    </AuthProvider>
  )
}

export default App
