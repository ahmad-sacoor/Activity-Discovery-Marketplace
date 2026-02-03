import type { Activity, Booking, CreateBookingRequest } from "@/types";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

async function handleJson<T>(res: Response): Promise<T> {
    if (!res.ok) {
        let message = `HTTP ${res.status}`;
        try {
            const data = await res.json();
            if (data && typeof data === "object" && "error" in data) {
                message = String((data as any).error);
            }
        } catch {
            // ignore
        }
        throw new Error(message);
    }
    return res.json();
}

export async function fetchActivities(filters?: {
    city?: string;
    category?: string;
    maxPrice?: string;
}): Promise<Activity[]> {
    const params = new URLSearchParams();
    if (filters?.city) params.set("city", filters.city);
    if (filters?.category) params.set("category", filters.category);
    if (filters?.maxPrice) params.set("maxPrice", filters.maxPrice);

    const qs = params.toString();
    const url = `${API_URL}/activities${qs ? `?${qs}` : ""}`;

    const res = await fetch(url, { cache: "no-store" });
    return handleJson<Activity[]>(res);
}

export async function createBooking(body: CreateBookingRequest): Promise<Booking> {
    const res = await fetch(`${API_URL}/bookings`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
    });
    return handleJson<Booking>(res);
}

export async function fetchBookings(userId: number): Promise<Booking[]> {
    const res = await fetch(`${API_URL}/bookings?userId=${userId}`, { cache: "no-store" });
    return handleJson<Booking[]>(res);
}

export async function cancelBooking(bookingId: number, userId: number): Promise<void> {
    const res = await fetch(`${API_URL}/bookings/${bookingId}?userId=${userId}`, {
        method: "DELETE",
    });

    // DELETE 204 typically has no JSON body
    if (!res.ok) {
        let message = `HTTP ${res.status}`;
        try {
            const data = await res.json();
            if (data && typeof data === "object" && "error" in data) {
                message = String((data as any).error);
            }
        } catch {
            // ignore
        }
        throw new Error(message);
    }
}

export function getApiUrlForDisplay() {
    return API_URL;
}
