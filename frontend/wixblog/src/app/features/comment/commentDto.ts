export interface CommentDto {
  id?: number;
  content: string;
  authorName: string;
  authorProfilePicture?: string;
  likeCount?: number;
  createdAt: string;
}
