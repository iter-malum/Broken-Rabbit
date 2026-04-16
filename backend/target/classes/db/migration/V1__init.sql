-- FILE: src/main/resources/db/migration/V1__init.sql
CREATE TABLE IF NOT EXISTS usr (
    id BIGSERIAL PRIMARY KEY,
    un VARCHAR(64) UNIQUE NOT NULL,
    ph VARCHAR(128) NOT NULL,
    s VARCHAR(64) NOT NULL DEFAULT 'x7k9_p@ss',
    cr_ts TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS rb_st (
    id BIGSERIAL PRIMARY KEY,
    u_id BIGINT REFERENCES usr(id) ON DELETE CASCADE,
    ear_dir INT NOT NULL DEFAULT -1,
    fur_c VARCHAR(16) DEFAULT '#8899AA',
    h_ang FLOAT DEFAULT 0.0,
    meta_json TEXT DEFAULT '{}'
);

CREATE TABLE IF NOT EXISTS ex_flgs (
    id BIGSERIAL PRIMARY KEY,
    u_id BIGINT REFERENCES usr(id) ON DELETE CASCADE,
    v_id VARCHAR(32) NOT NULL,
    is_solved BOOLEAN DEFAULT FALSE,
    solved_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(u_id, v_id)
);

CREATE INDEX idx_rb_st_uid ON rb_st(u_id);
CREATE INDEX idx_ex_flags_uid ON ex_flgs(u_id);

INSERT INTO usr (id, un, ph, s) VALUES (1, 'user1', 'password123', 'user_secret') ON CONFLICT (id) DO NOTHING;
INSERT INTO usr (id, un, ph, s) VALUES (2, 'admin', 'admin123', 'admin_secret') ON CONFLICT (id) DO NOTHING;

INSERT INTO rb_st (u_id, ear_dir, fur_c, h_ang, meta_json) VALUES (1, -1, '#8899AA', 0.0, '{"dir":-1,"locked":false}') ON CONFLICT (u_id) DO NOTHING;
INSERT INTO rb_st (u_id, ear_dir, fur_c, h_ang, meta_json) VALUES (2, -1, '#8899AA', 0.0, '{"dir":-1,"locked":false,"mood":"sad"}') ON CONFLICT (u_id) DO NOTHING;