// Маппинг категорий: английское название -> русское отображение
export const CATEGORY_NAMES: Record<string, string> = {
  'LIFEHACKS': 'Лайфхаки коммуникаций',
  'CHECKLISTS': 'Чек-листы',
  'GAMES': 'Игры для офиса',
  'AI_TOOLS': 'Доступы к ИИ',
  'COURSES': 'Курсы прокачки',
};

// Функция для получения русского названия категории
export const getCategoryDisplayName = (categoryKey: string): string => {
  return CATEGORY_NAMES[categoryKey] || categoryKey;
};

// Функция для получения английского ключа по русскому названию
export const getCategoryKey = (displayName: string): string => {
  const entry = Object.entries(CATEGORY_NAMES).find(([_, value]) => value === displayName);
  return entry ? entry[0] : displayName;
};