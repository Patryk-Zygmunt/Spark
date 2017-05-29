CREATE TABLE session_data (
    session_id TEXT PRIMARY_KEY,
    user_id INTEGER PRIMARY KEY,
    last_accessed TEXT,
    
    FOREIGN KEY (user_id) REFERENCES users(id)
);
