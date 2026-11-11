import React, { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import { Table } from '../components/Table';
import { Button } from '../components/Button';
import { Input } from '../components/Input';

export const NotesPage = () => {
    const [notes, setNotes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    // Загрузка заметок при открытии страницы
    const fetchNotes = async () => {
        try {
            setLoading(true);
            const response = await api.get('/notes');
            setNotes(response.data.content); // Spring отдает список в поле content
        } catch (error) {
            console.error('Ошибка при загрузке заметок:', error);
            alert('Не удалось загрузить заметки. Сервер запущен?');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchNotes();
    }, []);

    // Создание заметки
    const handleCreate = async (e) => {
        e.preventDefault();
        if (!title.trim()) return alert('Заголовок не может быть пустым');

        try {
            await api.post('/notes', { title, content });
            setTitle('');
            setContent('');
            fetchNotes(); // Обновляем список
        } catch (error) {
            console.error('Ошибка создания:', error);
            alert('Ошибка при создании заметки');
        }
    };

    // Удаление заметки
    const handleDelete = async (id) => {
        if (!window.confirm('Удалить эту заметку?')) return;

        try {
            await api.delete(`/notes/${id}`);
            fetchNotes(); // Обновляем список
        } catch (error) {
            console.error('Ошибка удаления:', error);
            alert('Не удалось удалить заметку');
        }
    };

    // Колонки для таблицы
    const columns = [
        { key: 'id', title: 'ID' },
        { key: 'title', title: 'Заголовок' },
        { key: 'content', title: 'Текст' },
        {
            key: 'actions',
            title: 'Действия',
            render: (row) => (
                <Button variant="danger" onClick={() => handleDelete(row.id)}>
                    Удалить
                </Button>
            )
        }
    ];

    if (loading) return <h2>Загрузка...</h2>;

    return (
        <div>
            <h2>Мои заметки</h2>

            {/* Форма создания */}
            <form onSubmit={handleCreate} style={{ marginBottom: '20px', padding: '15px', border: '1px solid #ccc', borderRadius: '5px' }}>
                <h3>Добавить заметку</h3>
                <Input
                    label="Заголовок"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="Введите заголовок"
                />
                <Input
                    label="Текст"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Введите текст"
                />
                <Button type="submit">Создать</Button>
            </form>

            {/* Таблица */}
            {notes.length === 0 ? (
                <p>У вас пока нет заметок.</p>
            ) : (
                <Table columns={columns} data={notes} />
            )}
        </div>
    );
};