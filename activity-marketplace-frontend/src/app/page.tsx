"use client";

import { useEffect, useMemo, useState } from "react";

type Activity = {
    id: number;
    title: string;
    city: string;
    category: string;
    price: number;
    rating: number | null;
    durationHours: number | null;
};

export default function Page() {
    const API_URL = useMemo(
        () => process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080",
        []
    );

    const [activities, setActivities] = useState<Activity[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                setError("");

                const res = await fetch(`${API_URL}/activities`);
                if (!res.ok) throw new Error(`HTTP ${res.status}`);

                const data = (await res.json()) as Activity[];
                setActivities(data);
            } catch (e: any) {
                setError(e?.message ?? "Failed to load activities");
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [API_URL]);

    return (
        <main style={styles.page}>
            <div style={styles.container}>
                <h1 style={styles.title}>Activity Marketplace</h1>
                <p style={styles.small}>
                    API: <code>{API_URL}</code>
                </p>

                {loading && <p>Loading…</p>}
                {error && <p style={styles.error}>Error: {error}</p>}

                <div style={styles.grid}>
                    {activities.map((a) => (
                        <div key={a.id} style={styles.card}>
                            <h2 style={styles.cardTitle}>{a.title}</h2>
                            <p style={styles.small}>
                                {a.city} • {a.category}
                            </p>
                            <p style={styles.small}>
                                €{a.price} • Rating: {a.rating ?? "—"} • {a.durationHours ?? "—"}h
                            </p>

                            <button
                                style={styles.button}
                                onClick={() => console.log("book activity", a.id)}
                            >
                                Book
                            </button>
                        </div>
                    ))}
                </div>

                {!loading && !error && activities.length === 0 && <p>No activities found.</p>}
            </div>
        </main>
    );
}

const styles: Record<string, React.CSSProperties> = {
    page: {
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        padding: "32px 16px",
        background: "#fafafa",
    },
    container: {
        width: "100%",
        maxWidth: 900,
    },
    title: {
        margin: 0,
        fontSize: 32,
    },
    small: {
        marginTop: 8,
        marginBottom: 12,
        color: "#444",
    },
    error: {
        color: "crimson",
    },
    grid: {
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(260px, 1fr))",
        gap: 16,
        marginTop: 12,
    },
    card: {
        border: "1px solid #ddd",
        borderRadius: 12,
        padding: 16,
        background: "white",
    },
    cardTitle: {
        margin: 0,
        marginBottom: 8,
        fontSize: 18,
    },
    button: {
        marginTop: 10,
        width: "100%",
        padding: "10px 12px",
        borderRadius: 10,
        border: "1px solid #333",
        background: "white",
        cursor: "pointer",
        fontWeight: 600,
    },
};
