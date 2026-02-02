"use client";

import { useEffect, useMemo, useState } from "react";
import type { Activity, Booking } from "@/types";
import { createBooking, fetchActivities, fetchBookings, getApiUrlForDisplay } from "@/lib/api";
import styles from "./page.module.css";

const USER_ID = 1;

export default function Page() {
    const API_URL = useMemo(() => getApiUrlForDisplay(), []);

    // Filters
    const [city, setCity] = useState("");
    const [category, setCategory] = useState("");
    const [maxPrice, setMaxPrice] = useState("");

    // Activities
    const [activities, setActivities] = useState<Activity[]>([]);
    const [activitiesLoading, setActivitiesLoading] = useState(true);
    const [activitiesError, setActivitiesError] = useState("");

    // Booking feedback
    const [bookingInFlightId, setBookingInFlightId] = useState<number | null>(null);

    // Bookings
    const [bookings, setBookings] = useState<Booking[]>([]);
    const [bookingsLoading, setBookingsLoading] = useState(false);
    const [bookingsError, setBookingsError] = useState("");

    // Notice
    const [notice, setNotice] = useState("");

    function showNotice(msg: string) {
        setNotice(msg);
        window.setTimeout(() => setNotice(""), 2400);
    }

    async function loadActivities(filters?: { city?: string; category?: string; maxPrice?: string }) {
        try {
            setActivitiesLoading(true);
            setActivitiesError("");
            const data = await fetchActivities(filters);
            setActivities(data);
        } catch (e: any) {
            setActivitiesError(e?.message ?? "Failed to load activities");
        } finally {
            setActivitiesLoading(false);
        }
    }

    useEffect(() => {
        loadActivities();
    }, []);

    async function onSearch() {
        const f: any = {};
        if (city.trim()) f.city = city.trim();
        if (category.trim()) f.category = category.trim();
        if (maxPrice.trim()) f.maxPrice = maxPrice.trim();
        await loadActivities(f);
    }

    async function onClear() {
        setCity("");
        setCategory("");
        setMaxPrice("");
        await loadActivities();
    }

    async function onBook(activityId: number) {
        try {
            setBookingInFlightId(activityId);
            await createBooking({ userId: USER_ID, activityId });
            showNotice("Booked!");
        } catch (e: any) {
            showNotice(`Booking failed: ${e?.message ?? "Unknown error"}`);
        } finally {
            setBookingInFlightId(null);
        }
    }

    async function onLoadMyBookings() {
        try {
            setBookingsLoading(true);
            setBookingsError("");
            const data = await fetchBookings(USER_ID);
            setBookings(data);
        } catch (e: any) {
            setBookingsError(e?.message ?? "Failed to load bookings");
        } finally {
            setBookingsLoading(false);
        }
    }

    return (
        <main className={styles.page}>
            <div className={styles.container}>
                <header className={styles.header}>
                    <div>
                        <h1 className={styles.title}>Activity Discovery Marketplace</h1>
                        <p className={styles.subtle}>
                            API: <span className={styles.code}>{API_URL}</span>
                        </p>
                    </div>
                    {notice && <div className={styles.notice}>{notice}</div>}
                </header>

                {/* Filters */}
                <section className={styles.section}>
                    <h2 className={styles.sectionTitle}>Search</h2>

                    <div className={styles.filtersRow}>
                        <div className={styles.field}>
                            <label className={styles.label}>City</label>
                            <input
                                className={styles.input}
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                placeholder="e.g. Lisbon"
                            />
                        </div>

                        <div className={styles.field}>
                            <label className={styles.label}>Category</label>
                            <input
                                className={styles.input}
                                value={category}
                                onChange={(e) => setCategory(e.target.value)}
                                placeholder="e.g. Food"
                            />
                        </div>

                        <div className={styles.field}>
                            <label className={styles.label}>Max Price</label>
                            <input
                                className={styles.input}
                                value={maxPrice}
                                onChange={(e) => setMaxPrice(e.target.value)}
                                placeholder="e.g. 50"
                                inputMode="decimal"
                            />
                        </div>

                        <div className={styles.actions}>
                            <button
                                className={`${styles.btnPrimary} ${activitiesLoading ? styles.btnDisabled : ""}`}
                                onClick={onSearch}
                                disabled={activitiesLoading}
                            >
                                Search
                            </button>
                            <button
                                className={`${styles.btnSecondary} ${activitiesLoading ? styles.btnDisabled : ""}`}
                                onClick={onClear}
                                disabled={activitiesLoading}
                            >
                                Clear
                            </button>
                        </div>
                    </div>

                    {activitiesError && <div className={styles.error}>Error: {activitiesError}</div>}
                </section>

                {/* Results */}
                <section className={styles.section}>
                    <h2 className={styles.sectionTitle}>Results</h2>

                    {activitiesLoading && <p className={styles.helperText}>Loading activities…</p>}
                    {!activitiesLoading && activities.length === 0 && (
                        <p className={styles.helperText}>No activities found.</p>
                    )}

                    <div className={styles.grid}>
                        {activities.map((a) => {
                            const busy = bookingInFlightId === a.id;

                            return (
                                <div key={a.id} className={styles.card}>
                                    <div>
                                        <h3 className={styles.cardTitle}>{a.title}</h3>
                                        <div className={styles.badges}>
                                            <span className={styles.badge}>{a.city}</span>
                                            <span className={styles.badge}>{a.category}</span>
                                        </div>
                                    </div>

                                    <div className={styles.meta}>
                                        <span>Price: €{a.price}</span>
                                        <span>Rating: {a.rating ?? "—"}</span>
                                        <span>Duration: {a.durationHours ?? "—"}h</span>
                                    </div>

                                    <button
                                        className={`${styles.bookBtn} ${busy ? styles.bookBtnBusy : ""}`}
                                        onClick={() => onBook(a.id)}
                                        disabled={busy}
                                    >
                                        {busy ? "Booking..." : "Book"}
                                    </button>
                                </div>
                            );
                        })}
                    </div>
                </section>

                {/* My bookings */}
                <section className={styles.section}>
                    <div className={styles.sectionHeader}>
                        <h2 className={styles.sectionTitle} style={{ marginBottom: 0 }}>
                            My Bookings
                        </h2>

                        <button
                            className={`${styles.btnSecondary} ${bookingsLoading ? styles.btnDisabled : ""}`}
                            onClick={onLoadMyBookings}
                            disabled={bookingsLoading}
                        >
                            {bookingsLoading ? "Loading..." : "Load My Bookings"}
                        </button>
                    </div>

                    {bookingsError && <div className={styles.error}>Error: {bookingsError}</div>}

                    {!bookingsLoading && bookings.length === 0 && (
                        <p className={styles.helperText}>No bookings yet. Book an activity to see it here.</p>
                    )}

                    <div className={styles.bookingsList}>
                        {bookings.map((b) => (
                            <div key={b.id} className={styles.bookingRow}>
                                <div>
                                    <div className={styles.bookingTitle}>
                                        #{b.id} • {new Date(b.bookedAt).toLocaleString()}
                                    </div>
                                    <div className={styles.bookingSub}>
                                        {b.activity.title} — {b.activity.city} — {b.activity.category}
                                    </div>
                                </div>
                                <div className={styles.bookingPrice}>€{b.activity.price}</div>
                            </div>
                        ))}
                    </div>
                </section>
            </div>
        </main>
    );
}
