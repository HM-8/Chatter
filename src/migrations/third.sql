ALTER TABLE `messages`
    RENAME COLUMN `file_id` TO `file`;
ALTER TABLE `messages`
    CHANGE COLUMN `content` `content` TEXT NULL ,
    CHANGE COLUMN `file` `file` LONGBLOB NULL ;
