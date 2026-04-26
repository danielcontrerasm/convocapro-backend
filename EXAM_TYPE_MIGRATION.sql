-- Run this once if you already have an existing database.
ALTER TABLE questions ADD COLUMN IF NOT EXISTS exam_type VARCHAR(50);
UPDATE questions SET exam_type = 'GENERIC' WHERE exam_type IS NULL;
