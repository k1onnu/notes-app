import React from 'react';
import { Routes, Route, NavLink } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Home } from './pages/Home';
import { NotesPage } from './pages/NotesPage';
import { NotFound } from './pages/NotFound';

function App() {
  return (
    <div>
      {/* Красивая навигационная панель */}
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-4 shadow-sm">
        <div className="container">
          <NavLink className="navbar-brand fw-bold" to="/">
           NA
          </NavLink>

          <div className="collapse navbar-collapse">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-item">
                <NavLink
                  className={({ isActive }) => isActive ? "nav-link active fw-bold" : "nav-link"}
                  to="/"
                >
                  Главная
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink
                  className={({ isActive }) => isActive ? "nav-link active fw-bold" : "nav-link"}
                  to="/notes"
                >
                  Заметки
                </NavLink>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      {/* Основной контент */}
      <div className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/notes" element={<NotesPage />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>

      {/* Контейнер для всплывающих уведомлений */}
      <ToastContainer position="bottom-right" autoClose={3000} />
    </div>
  );
}

export default App;
