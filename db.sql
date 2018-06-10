CREATE VIEW category_info AS
  SELECT
    array_append(b.parent_ids, b.id) AS category_arr,
    array_to_string(CASE WHEN count(*) = 1
      THEN array_agg(b.title)
                    ELSE array_append(array_agg(a.title), b.title)
                    END, ',')        AS title
  FROM category b LEFT JOIN category a ON a.id = ANY (b.parent_ids)
  GROUP BY b.id;;