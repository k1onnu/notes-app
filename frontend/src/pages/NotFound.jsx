import React from 'react';
import { Link } from 'react-router-dom';

export const NotFound = () => {
    return (
        <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h1>404 - Страница не найдена</h1>
            <Link to="/">Вернуться на главную</Link>
        </div>
    );
};