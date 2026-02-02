// src/types.ts

export type Activity = {
    id: number;
    title: string;
    city: string;
    category: string;
    price: number;
    rating: number | null;
    durationHours: number | null;
};

export type Booking = {
    id: number;
    userId: number;
    bookedAt: string; // ISO string
    activity: Activity;
};

export type CreateBookingRequest = {
    userId: number;
    activityId: number;
};
