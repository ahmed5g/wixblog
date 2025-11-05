export interface AuthResponse{
  id: number;
  email: string;
  name: string;
  profilePicture: string;
  role: string;
  authenticated: boolean;
  createdAt: string;
}
