CREATE TABLE folder_metadata (
    folder_id INTEGER(8) PRIMARY KEY,
    name TEXT,
    path_lower TEXT,
    path_display TEXT,
    parent_folder_id INTEGER(8),
    server_created_at TEXT
);
