import { MessageSquare, ThumbsUp, User } from 'lucide-react';

interface Post {
  id: string;
  title: string;
  author: string;
  date: string;
  views: number;
  likes: number;
  comments: number;
  category: string;
}

const posts: Post[] = [
  { id: '1', title: 'VMS 화면 표시 오류 해결 방법 공유', author: '김철수', date: '2027-02-20', views: 45, likes: 12, comments: 5, category: '기술' },
  { id: '2', title: 'CCTV 야간 촬영 최적화 팁', author: '이영희', date: '2027-02-19', views: 78, likes: 23, comments: 8, category: '기술' },
  { id: '3', title: '긴급전화 점검 체크리스트', author: '박민수', date: '2027-02-18', views: 62, likes: 18, comments: 4, category: '점검' },
  { id: '4', title: '동계 설비 관리 경험 공유', author: '최지우', date: '2027-02-17', views: 91, likes: 34, comments: 12, category: '경험' },
  { id: '5', title: '장애 대응 시 주의사항', author: '정민호', date: '2027-02-16', views: 103, likes: 41, comments: 15, category: '안전' },
  { id: '6', title: 'ITS 시스템 성능 개선 제안', author: '강서연', date: '2027-02-15', views: 67, likes: 19, comments: 7, category: '개선' },
  { id: '7', title: '신규 설비 교육 자료 요청', author: '윤태호', date: '2027-02-14', views: 54, likes: 11, comments: 6, category: '교육' },
  { id: '8', title: '현장 안전 관리 개선 방안', author: '임수진', date: '2027-02-13', views: 88, likes: 28, comments: 10, category: '안전' },
];

export function Board() {
  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">게시판</h1>
          <p className="text-sm text-gray-600 mt-1">업무 노하우와 정보를 공유하세요</p>
        </div>
        <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
          글쓰기
        </button>
      </div>

      {/* Categories */}
      <div className="flex items-center gap-2 mb-6">
        <button className="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm">전체</button>
        <button className="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg text-sm hover:bg-gray-50">기술</button>
        <button className="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg text-sm hover:bg-gray-50">점검</button>
        <button className="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg text-sm hover:bg-gray-50">경험</button>
        <button className="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg text-sm hover:bg-gray-50">안전</button>
        <button className="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg text-sm hover:bg-gray-50">개선</button>
        <button className="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg text-sm hover:bg-gray-50">교육</button>
      </div>

      {/* Popular Posts */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
        <h3 className="text-sm font-semibold text-gray-900 mb-3">인기 게시글</h3>
        <div className="space-y-2">
          {posts.slice(0, 3).map((post, index) => (
            <div key={post.id} className="flex items-center gap-3 text-sm">
              <span className="flex items-center justify-center w-6 h-6 bg-blue-600 text-white rounded-full text-xs font-bold">
                {index + 1}
              </span>
              <a href="#" className="text-gray-900 hover:text-blue-600 flex-1">
                {post.title}
              </a>
              <div className="flex items-center gap-3 text-xs text-gray-600">
                <span className="flex items-center gap-1">
                  <ThumbsUp className="w-3 h-3" />
                  {post.likes}
                </span>
                <span className="flex items-center gap-1">
                  <MessageSquare className="w-3 h-3" />
                  {post.comments}
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Posts List */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-20">번호</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-24">분류</th>
              <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">제목</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-32">작성자</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-32">작성일</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-24">조회</th>
              <th className="text-center py-3 px-4 text-sm font-semibold text-gray-700 w-24">추천</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post, index) => (
              <tr key={post.id} className="border-b border-gray-100 hover:bg-gray-50">
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{posts.length - index}</td>
                <td className="py-3 px-4 text-center">
                  <span className="px-2 py-1 bg-gray-100 text-gray-700 rounded text-xs font-medium">
                    {post.category}
                  </span>
                </td>
                <td className="py-3 px-4 text-sm text-gray-900">
                  <a href="#" className="hover:text-blue-600 flex items-center gap-2">
                    {post.title}
                    {post.comments > 0 && (
                      <span className="flex items-center gap-1 text-blue-600 text-xs">
                        <MessageSquare className="w-3 h-3" />
                        {post.comments}
                      </span>
                    )}
                  </a>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">
                  <div className="flex items-center justify-center gap-1">
                    <User className="w-3 h-3" />
                    {post.author}
                  </div>
                </td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{post.date}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">{post.views}</td>
                <td className="py-3 px-4 text-sm text-gray-600 text-center">
                  <div className="flex items-center justify-center gap-1">
                    <ThumbsUp className="w-3 h-3" />
                    {post.likes}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex items-center justify-center gap-2 mt-6">
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">이전</button>
        <button className="px-3 py-1 bg-blue-600 text-white rounded">1</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">2</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">3</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">4</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">5</button>
        <button className="px-3 py-1 border border-gray-300 rounded hover:bg-gray-50">다음</button>
      </div>
    </div>
  );
}
