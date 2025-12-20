export interface JwtPayload {
  sub: string;
  email: string;
  name: string;
  role: string;
  picture?: string;
  first_name: string;
  last_name: string;
  created_at: number;
  iat: number;
  exp: number;
}
