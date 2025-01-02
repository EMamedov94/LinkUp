let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
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
    const messageContainer = document.querySelector('#chat-messages ul');
    const messageElement = document.createElement('li');
    const messageText = document.createElement('p');
    const time = document.createElement('span');

    messageElement.classList.add('message', senderId === senderId ? 'sent' : 'received');
    time.classList.add('time');

    messageText.innerText = message.text;
    time.innerText = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    messageElement.append(messageText, time);
    messageContainer.appendChild(messageElement);
}

// Обработчик отправки формы
document.getElementById('chatForm').addEventListener('submit', function(event) {
    event.preventDefault();  // Отменяем стандартную отправку формы

    const messageText = document.getElementById('messageText').value;

    // Отправляем сообщение
    sendMessage(chatId, senderId, receiverId, messageText);
});

// Инициализация WebSocket при загрузке страницы
connect();