import React, { useState, useEffect } from 'react';
import { Routes, Route, NavLink, useNavigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Home } from './pages/Home';
import { NotesPage } from './pages/NotesPage';
import { NotFound } from './pages/NotFound';
import { LoginPage } from './pages/LoginPage';
import { ProtectedRoute } from './components/ProtectedRoute';

function App() {
  const [isAuth, setIsAuth] = useState(false);
  const [username, setUsername] = useState(''); // Состояние для логина
  const navigate = useNavigate();

  useEffect(() => {
    setIsAuth(!!localStorage.getItem('token'));
    setUsername(localStorage.getItem('username') || 'Пользователь'); // Берем логин
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username'); // Удаляем логин при выходе
    setIsAuth(false);
    navigate('/login');
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4 shadow-sm">
        <div className="container">
          <NavLink className="navbar-brand fw-bold" to="/">
          Notes App
          </NavLink>
          <div className="collapse navbar-collapse">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <NavLink className={({ isActive }) => isActive ? "nav-link active fw-bold" : "nav-link"} to="/">
                  Главная
                </NavLink>
              </li>
              {isAuth && (
                <li className="nav-item">
                  <NavLink className={({ isActive }) => isActive ? "nav-link active fw-bold" : "nav-link"} to="/notes">
                    Заметки
                  </NavLink>
                </li>
              )}
            </ul>

            <div className="d-flex align-items-center">
              {isAuth ? (
                <>
                  {/* Отображаем имя пользователя */}
                  <span className="text-light me-3 fw-bold">
                    👤 {username}
                  </span>
                  <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
                    Выйти
                  </button>
                </>
              ) : (
                <NavLink className="btn btn-outline-light" to="/login">
                  Войти
                </NavLink>
              )}
            </div>
          </div>
        </div>
      </nav>

      <div className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<LoginPage />} />

          {/* Защищенные маршруты */}
          <Route element={<ProtectedRoute />}>
            <Route path="/notes" element={<NotesPage />} />
          </Route>

          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>

      <ToastContainer position="bottom-right" autoClose={3000} />
    </div>
  );
}

export default App;
