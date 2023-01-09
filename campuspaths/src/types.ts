// represents a Building object, which contains a coordinate pair, and names
export interface Building {
    // coordinates
    x: number;
    y: number;
    shortName: string;
    longName: string;
}

// represents an Edge element, which contains two coordinate pairs
export interface Edge {
    x1: number;
    x2: number;
    y1: number;
    y2: number;
}

// represents  a Path Segment, which has a start, an end and a cost
export interface PathSegment {
    start: Point;
    end: Point;
    cost: number;
}

// represents a Point
export interface Point {
    x: number
    y: number
}