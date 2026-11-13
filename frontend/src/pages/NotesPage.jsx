import React, { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import { Table } from '../components/Table';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Modal } from '../components/Modal';
import { toast } from 'react-toastify';

export const NotesPage = () => {
    // Данные и пагинация
    const [notes, setNotes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [search, setSearch] = useState("");
    const [sortBy, setSortBy] = useState("id");

    // Форма
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    // Состояние для чтения заметки
    const [selectedNote, setSelectedNote] = useState(null);

    // Состояние для шаринга
    const [shareNoteId, setShareNoteId] = useState(null);
    const [shareUsername, setShareUsername] = useState('');
    const [shareCanEdit, setShareCanEdit] = useState(true);

    const fetchNotes = async () => {
        try {
            setLoading(true);
            const response = await api.get('/notes', {
                params: { page, size: 5, search, sortBy }
            });
            setNotes(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            toast.error('Не удалось загрузить заметки');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchNotes();
    }, [page, search, sortBy]);

    const handleCreate = async (e) => {
        e.preventDefault();
        if (!title.trim()) return toast.warning('Заголовок не может быть пустым');

        try {
            await api.post('/notes', { title, content });
            toast.success('Заметка создана!');
            setTitle('');
            setContent('');
            setPage(0);
            fetchNotes();
        } catch (error) {
            toast.error('Ошибка при создании заметки');
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Вы уверены, что хотите удалить эту заметку?')) return;

        try {
            await api.delete(`/notes/${id}`);
            toast.success('Заметка удалена');
            if (notes.length === 1 && page > 0) setPage(page - 1);
            fetchNotes();
        } catch (error) {
            toast.error('Не удалось удалить заметку');
        }
    };

    // Обработчик шаринга
    const handleShare = async (e) => {
        e.preventDefault();
        if (!shareUsername) return toast.warning('Введите имя пользователя');

        try {
            await api.post(`/notes/${shareNoteId}/share`, {
                username: shareUsername,
                canEdit: shareCanEdit
            });
            toast.success('Заметка успешно расшарена!');
            setShareNoteId(null);
            setShareUsername('');
            fetchNotes();
        } catch (error) {
            toast.error(error.response?.data?.message || 'Ошибка при шаринге');
        }
    };

        const columns = [
            { key: 'id', title: 'ID' },
            { key: 'title', title: 'Заголовок' },
            {
                key: 'ownerName',
                title: 'Владелец',
                render: (row) => {
                    const currentUser = localStorage.getItem('username');

                    // Если владелец не я — это общая заметка
                    if (row.ownerName !== currentUser) {
                        return (
                            <span className="badge bg-warning text-dark" title="Расшарено со мной">
                                🤝 {row.ownerName}
                            </span>
                        );
                    }

                    // Если это моя заметка
                    return <span className="badge bg-secondary">👤 Я</span>;
                }
            },
            {
                key: 'content',
                title: 'Текст',
                render: (row) => (
                    <div className="text-truncate" style={{ maxWidth: '200px' }} title={row.content}>
                        {row.content}
                    </div>
                )
            },
            {
                key: 'createdAt',
                title: 'Дата',
                render: (row) => {
                    if (!row.createdAt) return '-';
                    const date = new Date(row.createdAt);
                    return date.toLocaleString('ru-RU', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    });
                }
            },
            {
                key: 'actions',
                title: 'Действия',
                render: (row) => (
                    <div className="d-flex gap-1">
                        <Button variant="primary" onClick={() => setSelectedNote(row)}>
                            Читать
                        </Button>
                        <Button variant="primary" onClick={() => setShareNoteId(row.id)}>
                            Поделиться
                        </Button>
                        <Button variant="danger" onClick={() => handleDelete(row.id)}>
                            Удалить
                        </Button>
                    </div>
                )
            }
        ];

    return (
        <div>
            <h2>Мои заметки</h2>

            <form onSubmit={handleCreate} className="card p-4 mb-4 shadow-sm">
                <h4 className="mb-3">Добавить заметку</h4>
                <Input label="Заголовок" value={title} onChange={(e) => setTitle(e.target.value)} placeholder="Введите заголовок" />
                <Input label="Текст" value={content} onChange={(e) => setContent(e.target.value)} placeholder="Введите текст" />
                <Button type="submit">Создать заметку</Button>
            </form>

            <div className="d-flex gap-3 mb-3">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Поиск по заголовку..."
                    value={search}
                    onChange={(e) => { setSearch(e.target.value); setPage(0); }}
                />
                <select className="form-select w-25" value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
                    <option value="id">По ID</option>
                    <option value="title">По заголовку</option>
                    <option value="createdAt">По дате создания</option>
                </select>
            </div>

            {loading ? (
                <div className="text-center my-5">
                    <div className="spinner-border text-primary" role="status"></div>
                </div>
            ) : notes.length === 0 ? (
                <div className="alert alert-info">Заметок не найдено.</div>
            ) : (
                <Table columns={columns} data={notes} />
            )}

            {totalPages > 1 && (
                <div className="d-flex justify-content-between align-items-center mt-3">
                    <Button variant="primary" onClick={() => setPage(p => Math.max(0, p - 1))} disabled={page === 0}>Назад</Button>
                    <span>Страница {page + 1} из {totalPages}</span>
                    <Button variant="primary" onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))} disabled={page === totalPages - 1}>Вперед</Button>
                </div>
            )}

            {/* Модалка чтения */}
            <Modal
                isOpen={selectedNote !== null}
                onClose={() => setSelectedNote(null)}
                title={selectedNote?.title}
            >
                <div style={{ whiteSpace: 'pre-wrap' }}>
                    {selectedNote?.content}
                </div>
            </Modal>

            {/* Модалка шаринга */}
            <Modal
                isOpen={shareNoteId !== null}
                onClose={() => setShareNoteId(null)}
                title="Поделиться заметкой"
            >
                <form onSubmit={handleShare}>
                    <Input
                        label="Имя пользователя (кому даем доступ)"
                        value={shareUsername}
                        onChange={(e) => setShareUsername(e.target.value)}
                        placeholder="Введите логин (например, tester_a)"
                    />

                    <div className="form-check mb-3">
                        <input
                            className="form-check-input"
                            type="checkbox"
                            checked={shareCanEdit}
                            onChange={(e) => setShareCanEdit(e.target.checked)}
                            id="canEditCheck"
                        />
                        <label className="form-check-label" htmlFor="canEditCheck">
                            Разрешить редактирование (если снять галочку — только чтение)
                        </label>
                    </div>

                    <Button type="submit">Поделиться</Button>
                </form>
            </Modal>
        </div>
    );
};