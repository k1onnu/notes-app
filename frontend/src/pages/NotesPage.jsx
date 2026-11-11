import React from 'react';
import { Link } from 'react-router-dom';

export const NotesPage = () => {
    return (
        <div>
            <h2>Мои заметки</h2>
            <p>Здесь будет таблица с заметками и кнопка добавления.</p>
            <Link to="/">На главную</Link>
        </div>
    );
};