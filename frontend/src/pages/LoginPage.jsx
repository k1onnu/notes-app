import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import { Input } from '../components/Input';
import { Button } from '../components/Button';
import { toast } from 'react-toastify';

export const LoginPage = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!username || !password) return toast.warning('Заполните все поля');

        const endpoint = isLogin ? '/auth/login' : '/auth/register';

        try {
            const response = await api.post(endpoint, { username, password });

            localStorage.setItem('token', response.data.token);
            localStorage.setItem('username', username);

            toast.success(isLogin ? 'Успешный вход!' : 'Регистрация успешна!');
            navigate('/notes');
            window.location.reload();
        } catch (error) {
            toast.error(error.response?.data?.message || 'Ошибка авторизации');
        }
    };

    return (
        <div className="row justify-content-center mt-5">
            <div className="col-md-6">
                <div className="card shadow">
                    <div className="card-body p-4">
                        <h3 className="text-center mb-4">
                            {isLogin ? 'Вход в систему' : 'Регистрация'}
                        </h3>

                        <form onSubmit={handleSubmit}>
                            <Input
                                label="Логин"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder="Введите логин"
                            />

                            {/* Инпут пароля с кнопкой-глазиком */}
                            <Input
                                label="Пароль"
                                type={showPassword ? "text" : "password"} // Меняем тип
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Введите пароль"
                                rightElement={{
                                    content: showPassword ? '🙈' : '👁️',
                                    onClick: () => setShowPassword(!showPassword)
                                }}
                            />

                            <Button type="submit" className="w-100 mt-3">
                                {isLogin ? 'Войти' : 'Зарегистрироваться'}
                            </Button>
                        </form>

                        <div className="text-center mt-3">
                            <button
                                className="btn btn-link"
                                onClick={() => setIsLogin(!isLogin)}
                            >
                                {isLogin ? 'Нет аккаунта? Зарегистрируйтесь' : 'Уже есть аккаунт? Войдите'}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};