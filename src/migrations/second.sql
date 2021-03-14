ALTER TABLE `messages`
  RENAME COLUMN `message` TO `content`;
ALTER TABLE `messages`
  RENAME COLUMN `sent_from` TO `from`;
ALTER TABLE `messages`
  ADD COLUMN `to` int(11) NOT NULL;
ALTER TABLE `messages`
  ADD COLUMN `chat_id` int(11) NOT NULL;
ALTER TABLE `messages`
  ADD COLUMN `type` ENUM ('text', 'image', 'emoji', 'file');
ALTER TABLE `messages`
  ADD COLUMN `content` text NOT NULL;
ALTER TABLE `messages`
  ADD COLUMN `file_id` text NULL;
CREATE TABLE IF NOT EXISTS `chats` (
  `id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `type` ENUM ('user', 'group', 'channel')
);

CREATE TABLE IF NOT EXISTS `users_chats` (
  `user_id` int(11) NOT NULL,
  `chat_id` int(11) NOT NULL
);


ALTER TABLE `messages` ADD FOREIGN KEY (`from`) REFERENCES `users` (`id`);

ALTER TABLE `messages` ADD FOREIGN KEY (`to`) REFERENCES `chats` (`id`);

ALTER TABLE `users_chats` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `users_chats` ADD FOREIGN KEY (`chat_id`) REFERENCES `chats` (`id`);

