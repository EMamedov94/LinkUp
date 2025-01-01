let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function() {

        // Подписка на личные сообщения
        stompClient.subscribe('/user/queue/messages/' + chatId, function(message) {
            // Когда сообщение приходит, вызываем showMessage
            showMessage(JSON.parse(message.body));
        });
    }, function(error) {
        console.error('Ошибка подключения к WebSocket:', error);
    });
}

// Функция для отправки сообщений через WebSocket
function sendMessage(chatId, senderId, receiverId, messageText) {
    const message = {
        chatRoomId: chatId,
        senderId: senderId,
        receiverId: receiverId,
        text: messageText
    };

    if (stompClient !== null) {
        // Отправляем сообщение на сервер через WebSocket
        stompClient.send(`/app/sendMessage/` + chatId, {}, JSON.stringify(message));

        // Очищаем поле ввода после отправки
        document.getElementById('messageText').value = '';

        showMessage(message);
    } else {
        console.error('WebSocket соединение не установлено.');
    }
}

// Функция для отображения полученных сообщений
function showMessage(message) {
    // Находим контейнер для сообщений
    const messageContainer = document.querySelector('#chat-messages ul');

    // Создаем новый элемент списка для сообщения
    const messageElement = document.createElement('li');
    if (message.senderId === senderId) {
        messageElement.classList.add('message', 'sent'); // Стиль для отправленных сообщений
    } else {
        messageElement.classList.add('message', 'received'); // Стиль для полученных сообщений
    }

    // Создаем элемент для текста сообщения и добавляем его в li
    const messageText = document.createElement('p');
    const time = document.createElement('span');

    time.classList.add('time');
    messageText.innerText = message.text;

    messageElement.appendChild(messageText);
    messageElement.appendChild(time);

    // Добавляем новый элемент в контейнер сообщений
    messageContainer.appendChild(messageElement);
}

// Обработчик отправки формы
document.getElementById('chatForm').addEventListener('submit', function(event) {
    event.preventDefault();  // Отменяем стандартную отправку формы

    // Получаем текст сообщения из поля ввода
    const messageText = document.getElementById('messageText').value;

    // Отправляем сообщение
    sendMessage(chatId, senderId, receiverId, messageText);
});

// Инициализация WebSocket при загрузке страницы
connect();