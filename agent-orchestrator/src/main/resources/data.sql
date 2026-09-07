-- Insert a dummy user representing our Android Device
INSERT INTO app_users (device_id, created_at) 
VALUES ('dummy-android-device-123', CURRENT_TIMESTAMP);

-- Insert a dummy chat thread for that user
INSERT INTO chat_threads (thread_id, device_id, title, created_at, updated_at) 
VALUES ('dummy-thread-abc', 'dummy-android-device-123', 'My First Mumbai Trip', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
