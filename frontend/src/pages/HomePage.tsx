import { Link, useNavigate } from 'react-router-dom';
import { BookOpen, Users, Gamepad2, Bot, GraduationCap, Zap } from 'lucide-react';
import Button from '../components/ui/Button';

export default function HomePage() {
  const navigate = useNavigate();

  const handleCategoryClick = (category: string) => {
    // Переходим на страницу каталога с выбранной категорией
    navigate('/products', { state: { selectedCategory: category } });
  };

  return (
    <div>
      {/* Hero Section */}
      <section style={{
        background: 'linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%)',
        color: 'white',
        padding: '4rem 0',
        marginBottom: '4rem',
      }}>
        <div className="container" style={{ textAlign: 'center' }}>
          <h1 style={{
            fontSize: '3rem',
            marginBottom: '1rem',
            color: 'white',
          }}>
            Интернет-магазин «Спавн в IT»
          </h1>
          <p style={{
            fontSize: '1.25rem',
            marginBottom: '1rem',
            opacity: 0.95,
            maxWidth: '800px',
            margin: '0 auto 1rem',
            lineHeight: '1.6',
          }}>
            Работа в ИТ – это не только богатая беззаботная жизнь.
            Это постоянная прокачка скиллов, выживание на собесах и борьба за место под солнцем.
          </p>
          <p style={{
            fontSize: '1.1rem',
            marginBottom: '2rem',
            opacity: 0.9,
            maxWidth: '700px',
            margin: '0 auto 2rem',
          }}>
            <strong>Но сколько можно полагаться только на себя?</strong><br />
            Учитесь извлекать выгоду из всего, что вас окружает!
          </p>
          <Link to="/products">
            <Button
              variant="primary"
              size="lg"
              style={{
                backgroundColor: 'white',
                color: 'var(--primary)',
                fontSize: '1.1rem',
                padding: '1rem 2rem',
              }}
            >
              <Zap size={24} />
              Прокачать скиллы
            </Button>
          </Link>
        </div>
      </section>

      {/* Intro Section */}
      <section className="container" style={{ marginBottom: '4rem', textAlign: 'center' }}>
        <h2 style={{
          fontSize: '2rem',
          marginBottom: '1.5rem',
          color: 'var(--text-primary)',
        }}>
          Услуги для неопытных айтишников
        </h2>
        <p style={{
          fontSize: '1.1rem',
          color: 'var(--text-secondary)',
          maxWidth: '800px',
          margin: '0 auto',
          lineHeight: '1.8',
        }}>
          Мы предлагаем решения сложных технических и социальных задач.<br />
          Всё, что нужно для выживания и процветания в IT-индустрии!
        </p>
      </section>

      {/* Features - Центрированная раскладка */}
      <section className="container" style={{ marginBottom: '4rem' }}>
        <h2 style={{ textAlign: 'center', marginBottom: '3rem', fontSize: '2rem' }}>
          Что мы предлагаем
        </h2>


        {/* Первый ряд - 2 элемента по центру */}
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          gap: '2rem',
          marginBottom: '2rem',
          flexWrap: 'wrap',
        }}>
          {/* Лайфхаки */}
          <div
            onClick={() => handleCategoryClick('LIFEHACKS')}
            style={{
              textAlign: 'center',
              padding: '2rem',
              background: 'white',
              borderRadius: '1rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              transition: 'all 0.2s',
              cursor: 'pointer',
              width: '300px',
              maxWidth: '100%',
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
            }}
          >
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#dbeafe',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1.5rem',
              color: 'var(--primary)',
            }}>
              <Users size={40} />
            </div>
            <h3 style={{
              marginBottom: '1rem',
              fontSize: '1.3rem',
              color: 'var(--text-primary)',
            }}>
              Лайфхаки коммуникаций
            </h3>
            <p style={{
              color: 'var(--text-secondary)',
              fontSize: '0.95rem',
              lineHeight: '1.6',
            }}>
              От "Как не быть нитакусей" до "Шутки с начальством: норм или стрем?"
            </p>
          </div>

          {/* Чек-листы */}
          <div
            onClick={() => handleCategoryClick('CHECKLISTS')}
            style={{
              textAlign: 'center',
              padding: '2rem',
              background: 'white',
              borderRadius: '1rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              transition: 'all 0.2s',
              cursor: 'pointer',
              width: '300px',
              maxWidth: '100%',
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
            }}
          >
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#d1fae5',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1.5rem',
              color: 'var(--success)',
            }}>
              <BookOpen size={40} />
            </div>
            <h3 style={{
              marginBottom: '1rem',
              fontSize: '1.3rem',
              color: 'var(--text-primary)',
            }}>
              Чек-листы
            </h3>
            <p style={{
              color: 'var(--text-secondary)',
              fontSize: '0.95rem',
              lineHeight: '1.6',
            }}>
              На испытательный срок для всех специалистов. Спойлер: закентиться с HRкой – общий пункт!
            </p>
          </div>
        </div>


        {/* Второй ряд - 3 элемента по центру */}
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          gap: '2rem',
          flexWrap: 'wrap',
        }}>
          {/* Игры */}
          <div
            onClick={() => handleCategoryClick('GAMES')}
            style={{
              textAlign: 'center',
              padding: '2rem',
              background: 'white',
              borderRadius: '1rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              transition: 'all 0.2s',
              cursor: 'pointer',
              width: '300px',
              maxWidth: '100%',
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
            }}
          >
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#fef3c7',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1.5rem',
              color: 'var(--warning)',
            }}>
              <Gamepad2 size={40} />
            </div>
            <h3 style={{
              marginBottom: '1rem',
              fontSize: '1.3rem',
              color: 'var(--text-primary)',
            }}>
              Игры для офиса
            </h3>
            <p style={{
              color: 'var(--text-secondary)',
              fontSize: '0.95rem',
              lineHeight: '1.6',
            }}>
              От мафии до покера. Играем на зп начальника и увольнение нелюбимой коллеги!
            </p>
          </div>

          {/* ИИшки */}
          <div
            onClick={() => handleCategoryClick('AI_TOOLS')}
            style={{
              textAlign: 'center',
              padding: '2rem',
              background: 'white',
              borderRadius: '1rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              transition: 'all 0.2s',
              cursor: 'pointer',
              width: '300px',
              maxWidth: '100%',
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
            }}
          >
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#e9d5ff',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1.5rem',
              color: '#9333ea',
            }}>
              <Bot size={40} />
            </div>
            <h3 style={{
              marginBottom: '1rem',
              fontSize: '1.3rem',
              color: 'var(--text-primary)',
            }}>
              Доступы к ИИ
            </h3>
            <p style={{
              color: 'var(--text-secondary)',
              fontSize: '0.95rem',
              lineHeight: '1.6',
            }}>
              Доступы ко всем платным зарубежным чат-ботам. Пока вы думаете, начальник с GPT решает кого уволить!
            </p>
          </div>


          {/* Курсы */}
          <div
            onClick={() => handleCategoryClick('COURSES')}
            style={{
              textAlign: 'center',
              padding: '2rem',
              background: 'white',
              borderRadius: '1rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              transition: 'all 0.2s',
              cursor: 'pointer',
              width: '300px',
              maxWidth: '100%',
            }}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-5px)';
              e.currentTarget.style.boxShadow = '0 4px 16px rgba(0,0,0,0.15)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
            }}
          >
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              backgroundColor: '#fee2e2',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              margin: '0 auto 1.5rem',
              color: 'var(--danger)',
            }}>
              <GraduationCap size={40} />
            </div>
            <h3 style={{
              marginBottom: '1rem',
              fontSize: '1.3rem',
              color: 'var(--text-primary)',
            }}>
              Курсы прокачки
            </h3>
            <p style={{
              color: 'var(--text-secondary)',
              fontSize: '0.95rem',
              lineHeight: '1.6',
            }}>
              Курсы на все грейды всех должностей. Не ждите обучения от компании – берите всё в свои руки!
            </p>
          </div>
        </div>
      </section>

      {/* Call to Action */}
      <section style={{
        background: 'linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%)',
        padding: '4rem 0',
        textAlign: 'center',
      }}>
        <div className="container">
          <h2 style={{
            fontSize: '2rem',
            marginBottom: '1rem',
            color: 'var(--text-primary)',
          }}>
            Готовы прокачаться?
          </h2>
          <p style={{
            fontSize: '1.1rem',
            color: 'var(--text-secondary)',
            marginBottom: '2rem',
            maxWidth: '600px',
            margin: '0 auto 2rem',
          }}>
            Главное – не сдаваться! Пройдите этап естественного отбора и получите оффер мечты.
          </p>
          <Link to="/products">
            <Button
              variant="primary"
              size="lg"
              style={{
                fontSize: '1.1rem',
                padding: '1rem 2.5rem',
              }}
            >
              <Zap size={24} />
              Перейти в каталог
            </Button>
          </Link>
        </div>
      </section>
    </div>
  );
}